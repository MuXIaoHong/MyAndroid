package com.nan.myandroid

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.nan.myandroid.coroutines.KotlinActivity
import com.nan.myandroid.dialog_popupwindow.PopupWindowAndDialogActivity
import com.nan.myandroid.downloadmanager.DownloadManagerActivity
import com.nan.myandroid.databinding.DataBindingActivity
import com.nan.myandroid.keep_alive.ApplyRightActivity
import com.nan.myandroid.rengwuxian.view.CustomViewActivity
import com.nan.myandroid.transition_animation.TransitionActivity
import com.nan.myandroid.viewmodel.ViewModelActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initClick()
    }

    private fun initClick() {
        with(webview){
            settings.run {
                javaScriptEnabled = true
                domStorageEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                textZoom = 100
            }
            webViewClient= object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }
            }
            loadUrl("https://cloud13.3dnest.cn/play/?m=c1748e82_Zt8n_b6f9")
        }

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_work_manager -> {
                DownloadManagerActivity.launch(this)

            }
            R.id.btn_kotlin -> {
                KotlinActivity.launch(this)
            }
            R.id.btn_popupwindow_dialog -> {
                PopupWindowAndDialogActivity.launch(this)
            }
            R.id.btn_hencoder_view -> {
                CustomViewActivity.launch(this)
            }
            R.id.btn_viewmodel -> {
                ViewModelActivity.launch(this)
            }
            R.id.btn_livedata -> {
                DataBindingActivity.launch(this)
            }
            R.id.btn_transition_animation -> {
                TransitionActivity.launch(this)
            }
            R.id.btn_keep_alive -> {
                ApplyRightActivity.launch(this)
            }
            R.id.btn_network->{

            }
        }


    }
}