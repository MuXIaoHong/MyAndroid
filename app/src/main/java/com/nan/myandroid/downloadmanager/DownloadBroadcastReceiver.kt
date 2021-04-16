package com.nan.myandroid.downloadmanager

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import com.nan.myandroid.MyApplication
import com.nan.myandroid.app.AppUtils
import java.io.File

/**
 *author：93289
 *date:2020/7/1
 *dsc:
 */
class DownloadBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {
                Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show()
//                val fileInfo = DownLoadManagerUtil.queryResultById()
                MyApplication.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                val path= File(MyApplication.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"/${DownLoadManagerUtil.subPath}").path
                AppUtils.installApp(path)

            }
            DownloadManager.ACTION_NOTIFICATION_CLICKED -> {
                Toast.makeText(context, "点击通知栏", Toast.LENGTH_SHORT).show()

            }
        }

    }
}