package com.android.oss_speed_test

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 *author：93289
 *date:2021/1/21
 *dsc:
 */
object DownLoadManager {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().apply {
        }.build()
    }

    fun downFile(localPath: String, fileUrl: String,back:(Int)->Unit): Boolean {
        val request = Request.Builder()
            .url(fileUrl)
            .addHeader("Connection", "close")
            .build()
        val response = okHttpClient.newCall(request).execute()
        if (response.isSuccessful) {
            var inputStream: InputStream? = null
            val buf = ByteArray(2048)
            var len = 0
            var fos: FileOutputStream? = null
            // 储存下载文件的目录
            try {
                inputStream = response.body()!!.byteStream()
                val total = response.body()!!.contentLength()
                val file = File(localPath)
                fos = FileOutputStream(file)
                var sum: Long = 0
                while (inputStream.read(buf).also { len = it } != -1) {
                    fos.write(buf, 0, len)
                    sum += len.toLong()
                    val progress = (sum * 1.0f / total * 100).toInt()
                    back(progress)
                }
                fos.flush()
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                }
                try {
                    fos?.close()
                } catch (e: IOException) {
                }
            }
        } else {
            return false
        }
        return true
    }
}