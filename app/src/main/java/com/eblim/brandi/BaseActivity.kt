package com.eblim.brandi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    private var mActivityAnimationType: Int = 0
    val SLIDE_LEFT_IN_RIGHT_OUT = 1
    val SLIDE_RIGHT_IN_LEFT_OUT = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivityAnimation()
        setActivityAnimationType()
    }

    private fun startActivityAnimation() {
        when (mActivityAnimationType) {
            SLIDE_LEFT_IN_RIGHT_OUT -> overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right
            )
            SLIDE_RIGHT_IN_LEFT_OUT -> overridePendingTransition(
                R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left
            )
        }
    }

    private fun finishActivityAnimation() {
        when (mActivityAnimationType) {
            SLIDE_LEFT_IN_RIGHT_OUT -> overridePendingTransition(
                R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left
            )
            SLIDE_RIGHT_IN_LEFT_OUT -> overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right
            )
        }
    }

    override fun finish() {
        super.finish()
        finishActivityAnimation()
    }

    private fun setActivityAnimationType() {
        if (this is MainActivity ||
            this is ImageActivity) {
            mActivityAnimationType = SLIDE_RIGHT_IN_LEFT_OUT
        }
    }

}