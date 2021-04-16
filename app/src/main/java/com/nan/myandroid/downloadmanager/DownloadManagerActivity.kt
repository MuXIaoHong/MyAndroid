package com.nan.myandroid.downloadmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nan.myandroid.R
import kotlinx.android.synthetic.main.activity_downloadmanager.*


class DownloadManagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloadmanager)
        initClick()

    }

    private fun initClick() {

        btn_start.setOnClickListener {
            startDownloadManager()
        }
    }

    private fun startDownloadManager() {
        DownLoadManagerUtil.startDownloadManager(this)
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, DownloadManagerActivity::class.java)
            context.startActivity(intent)
        }
    }
}