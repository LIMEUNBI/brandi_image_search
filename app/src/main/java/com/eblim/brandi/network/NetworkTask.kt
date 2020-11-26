package com.eblim.brandi.network

import android.os.AsyncTask
import com.eblim.brandi.common.RequestListener
import com.eblim.brandi.utils.ImageParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class NetworkTask(searchUrl: String, requestListener: RequestListener) : AsyncTask<String, Void, String>() {

    private var searchUrl = searchUrl
    private var requestListener = requestListener
    var APP_KEY = "KakaoAK ff3fb548a624b5a91b67538a67dcffa8"
    var host = "dapi.kakao.com"

    override fun doInBackground(vararg p0: String?): String {
        val url = URL(searchUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Host", host)
        connection.setRequestProperty("Authorization", APP_KEY)

        val responseCode = connection.responseCode
        if (responseCode == 200) {
            BufferedReader(InputStreamReader(connection.inputStream, Charset.forName("UTF-8"))).use { reader ->
                return reader.readLine()
            }
        }
        return ""
    }

    override fun onPostExecute(response: String) {
        ImageParser().imgParser(response, requestListener)
    }
}