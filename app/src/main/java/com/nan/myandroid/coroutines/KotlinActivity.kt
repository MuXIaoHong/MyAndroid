package com.nan.myandroid.coroutines

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nan.myandroid.MyApplication.Companion.context
import com.nan.myandroid.R
import com.nan.myandroid.downloadmanager.DownloadManagerActivity
import kotlinx.android.synthetic.main.activity_kotlin.*

/**
 * {@link android.app.AppComponentFactory}
 */
class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        btn_basics.setOnClickListener {
            BasicsActivity.launch(this)
        }
        btn_cancel_timeout.setOnClickListener {
            CancelTimeoutActivity.launch(this)
        }
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, KotlinActivity::class.java)
            context.startActivity(intent)
        }
    }
}
