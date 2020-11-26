package com.eblim.brandi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import coil.api.load
import com.eblim.brandi.common.Config


class ImageActivity : BaseActivity() {

    private lateinit var mImg: ImageView
    private lateinit var mTxtSiteName: TextView
    private lateinit var mTxtDateTime: TextView

    private lateinit var mTxtMove: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        mImg = findViewById(R.id.img)
        mTxtSiteName = findViewById(R.id.display_site)
        mTxtDateTime = findViewById(R.id.datetime)
        mTxtMove = findViewById(R.id.txt_move)

        var intent: Intent = intent

        mImg.load(intent.getStringExtra(Config.IMAGE_URL))
        val display = windowManager.defaultDisplay
        val width = display.width
        val height = display.height

        mImg.minimumWidth = width
        mImg.minimumHeight = height

        mImg.maxWidth = width
        mImg.maxHeight = height

        mImg.scaleType = ImageView.ScaleType.FIT_XY
        mImg.adjustViewBounds = true

        mTxtMove.setOnClickListener {
            var docUrl = getIntent().getStringExtra(Config.DOC_URL)
            if (!TextUtils.isEmpty(docUrl)) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(docUrl))
                startActivity(intent)
            }
        }

        if (TextUtils.isEmpty(intent.getStringExtra(Config.SITE_NAME))) {
            mTxtSiteName.visibility = View.GONE
        } else {
            mTxtSiteName.visibility = View.VISIBLE
        }

        if (TextUtils.isEmpty(intent.getStringExtra(Config.DATE_TIME))) {
            mTxtDateTime.visibility = View.GONE
        } else {
            mTxtDateTime.visibility = View.VISIBLE
        }

        mTxtSiteName.text = intent.getStringExtra(Config.SITE_NAME)
        mTxtDateTime.text = intent.getStringExtra(Config.DATE_TIME)

    }

}