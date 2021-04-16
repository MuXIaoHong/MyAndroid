package com.nan.myandroid.app

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.nan.myandroid.MyApplication
import java.io.File

/**
 *author：93289
 *date:2020/7/1
 *dsc:
 */
object AppUtils {
    fun installApp(path: String) {
        val file = File(path)
        if (!file.exists()) return
        val intent = Intent(Intent.ACTION_VIEW)
        val data: Uri
        val type = "application/vnd.android.package-archive"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file)
        } else {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val authority: String =
                MyApplication.context.packageName.toString() + ".FileProvider"
            data = FileProvider.getUriForFile(MyApplication.context, authority, file)
        }
        intent.setDataAndType(data, type)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        MyApplication.context.startActivity(intent)
    }

}