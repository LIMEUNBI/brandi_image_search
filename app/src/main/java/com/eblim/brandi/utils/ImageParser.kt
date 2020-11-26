package com.eblim.brandi.utils

import com.eblim.brandi.common.RequestListener
import com.eblim.brandi.model.ImageInfo
import org.json.JSONArray
import org.json.JSONObject


class ImageParser {

    fun imgParser(response: String, requestListener: RequestListener) {
        try {
            var imageInfoList: ArrayList<ImageInfo> = ArrayList()
            val json = JSONObject(response)

            var documents = json.get("documents") as JSONArray
            if (documents.length() == 0) {
                requestListener.onRequestSuccess(0, imageInfoList, true)
            }

            var meta = json.get("meta") as JSONObject
            var isEnd = meta.get("is_end") as Boolean

            var length = documents.length()
            for (i in 0 until length) {
                var imageInfo = ImageInfo()
                var item: JSONObject = documents[i] as JSONObject
                var datetime = item.get("datetime")
                var sitename = item.get("display_sitename")
                var imageUrl = item.get("image_url")
                var docUrl = item.get("doc_url")

                imageInfo.image_url = imageUrl as String
                imageInfo.dateTime = datetime as String
                imageInfo.display_sitename = sitename as String
                imageInfo.doc_url = docUrl as String

                imageInfoList.add(imageInfo)

            }
            requestListener.onRequestSuccess(0, imageInfoList, isEnd)
        } catch (e: Exception) {
            requestListener.onRequestFailure(Throwable())
        }
    }
}