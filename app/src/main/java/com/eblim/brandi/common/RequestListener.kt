package com.eblim.brandi.common

interface RequestListener {

    fun onRequestSuccess(requestCode: Int, result: Any?, isEnd: Boolean)

    fun onRequestFailure(t: Throwable?)
}