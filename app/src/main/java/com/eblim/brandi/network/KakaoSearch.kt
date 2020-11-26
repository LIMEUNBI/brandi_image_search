package com.eblim.brandi.network

import com.eblim.brandi.common.RequestListener
import com.eblim.brandi.model.ImageInfo as ImageInfo

class KakaoSearch {

    private var searchURL = "https://dapi.kakao.com/v2/search/image?"

    fun getSearchList(searchWord: String, page: Int, size: Int, requestListener: RequestListener): ArrayList<ImageInfo> {
        var imageInfoList: ArrayList<ImageInfo> = ArrayList()
        var searchUrl = searchURL + "query=$searchWord&page=$page&size=$size"

        var networkTask = NetworkTask(searchUrl, requestListener)
        networkTask.execute()

        return imageInfoList
    }
}