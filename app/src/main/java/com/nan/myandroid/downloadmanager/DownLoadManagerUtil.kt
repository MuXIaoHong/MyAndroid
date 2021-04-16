package com.nan.myandroid.downloadmanager

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.nan.myandroid.MyApplication

/**
 *author：93289
 *date:2020/7/1
 *dsc:
 */
object DownLoadManagerUtil {
    var downloadManagerId: Long? = null
    lateinit var mDownloadManager: DownloadManager
    const val subPath="allfun.apk"
    fun startDownloadManager(context: Context) {
        mDownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request =
            DownloadManager.Request(Uri.parse("http://download-sdk.oss-cn-beijing.aliyuncs.com/downloads/imsdkdemo_android-3.6.9.1.apk"))
                .apply {
                    //设置允许网络，默认全部
                    //setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                    //设置通知栏显示模式
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    //设置通知栏Title
                    setTitle("趣随行")
                    setDestinationInExternalFilesDir(MyApplication.context,Environment.DIRECTORY_DOWNLOADS,subPath)
                    //设置描述
                    setDescription("APK正在下载")
                }
        //运行request
        downloadManagerId = mDownloadManager.enqueue(request)
        val intentFilter = IntentFilter().apply {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        }
        context.registerReceiver(DownloadBroadcastReceiver(), intentFilter)
    }

    fun queryResultById(): DownloadFileInfo? {
        downloadManagerId?.let {
            val query = DownloadManager.Query().setFilterById(it)
            val cursor = mDownloadManager.query(query)
            while (cursor.moveToNext()) {
                val bytesDownload =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                val descrition =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION))
                val id =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_ID))
                val localUri =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                val mimeType =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE))
                val title =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                val status =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val totalSize =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                Log.i("MainActivity", "bytesDownload:$bytesDownload")
                Log.i("MainActivity", "descrition:$descrition")
                Log.i("MainActivity", "id:$id")
                Log.i("MainActivity", "localUri:$localUri")
                Log.i("MainActivity", "mimeType:$mimeType")
                Log.i("MainActivity", "title:$title")
                Log.i("MainActivity", "status:$status")
                Log.i("MainActivity", "totalSize:$totalSize")
                return DownloadFileInfo(
                    bytesDownload,
                    descrition,
                    localUri,
                    mimeType,
                    title,
                    status,
                    totalSize
                )
            }
            return null
        }
        return null
    }


    fun getUriForDownloadedFile(): Uri? {
        downloadManagerId?.let {
            return mDownloadManager.getUriForDownloadedFile(it)
        }
        return null
    }
}


data class DownloadFileInfo(
    val currentBytes: String,
    val description: String,
    val localUri: String,
    val mimeType: String,
    val title: String,
    val status: String,
    val totalSize: String
)