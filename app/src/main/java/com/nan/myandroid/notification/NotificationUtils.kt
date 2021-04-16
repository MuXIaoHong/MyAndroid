package com.nan.myandroid.notification

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.nan.myandroid.MyApplication

/**
 *author：93289
 *date:2020/7/6
 *dsc:
 */
object NotificationUtils {

    fun areNotificationsEnabled(): Boolean {
        return NotificationManagerCompat.from(MyApplication.context).areNotificationsEnabled()
    }

    /**
     * 跳转应用通知设置页面
     */
    fun launchAppNotificationSetting() {
        val applicationInfo = MyApplication.context.applicationInfo
        val packageName = MyApplication.context.packageName
        val intent: Intent = Intent()

        try {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, applicationInfo.uid)
                    //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                    intent.putExtra("app_package", packageName)
                    intent.putExtra("app_uid", applicationInfo.uid)
                }
                Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT -> {
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.data = Uri.parse(
                        "package:$packageName"
                    )
                }
                else -> {
                    //其他低版本或者异常情况，走该节点。进入APP设置界面
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.putExtra("package", packageName)
                }
            }
        } catch (e: Exception) {
            //其他低版本或者异常情况，走该节点。进入APP设置界面
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.putExtra("package", packageName)
        } finally {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            MyApplication.context.startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setNotificationSetting(channelId: String) {
        val applicationInfo = MyApplication.context.applicationInfo
        val packageName = MyApplication.context.packageName
        val intent = Intent()
        try {

            //8.0及以后版本使用这两个extra.  >=API 26
            intent.action = Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            MyApplication.context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
