package com.nan.myandroid.keep_alive

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nan.myandroid.R

const val REQUEST_CODE_BACKGROUND_RUN = 10

class ApplyRightActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_right)
    }


    //申请后台运行电池优化忽略开始======
    // <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun isIgnoringBatteryOptimizations(): Boolean {
        var isIgnoring = false
        val powerManager =
            getSystemService(POWER_SERVICE) as PowerManager
        isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName)
        return isIgnoring
    }

    @SuppressLint("BatteryLife")
    fun requestIgnoreBatteryOptimizations() {
        try {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:$packageName")
            startActivityForResult(intent, REQUEST_CODE_BACKGROUND_RUN)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("NewApi")
    private fun applyIgnoreBatteryRight() {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setMessage(getString(R.string.account_for_request_battery_ignore))
            .setPositiveButton(
                getString(R.string.confirm)
            ) { _, _ -> requestIgnoreBatteryOptimizations()}
            .setNegativeButton(
                getString(R.string.cancel)
            ) { _, _ ->  }
            .create().show()

    }

    //======申请后台运行电池优化忽略结束
    @RequiresApi(Build.VERSION_CODES.M)
    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_apply_battery_ignoring -> {
                if (!isIgnoringBatteryOptimizations()) {
                    applyIgnoreBatteryRight()
                }
            }
            R.id.btn_request_white_list->{
                WhiteListUtils.enterWhiteListSetting()
            }
        }
    }
    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, ApplyRightActivity::class.java)
            context.startActivity(intent)
        }
    }
}