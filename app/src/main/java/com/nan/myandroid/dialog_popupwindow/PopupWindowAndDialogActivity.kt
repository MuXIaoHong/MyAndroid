package com.nan.myandroid.dialog_popupwindow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.nan.myandroid.R
import com.nan.myandroid.downloadmanager.DownloadManagerActivity
import kotlinx.android.synthetic.main.activity_popup_window_and_dialog.*

class PopupWindowAndDialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_window_and_dialog)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_pw_01 -> {
                PopupWindow().apply {
                    contentView = layoutInflater.inflate(R.layout.popup_window_simple, null)
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    isOutsideTouchable = true
                    isFocusable=false
                }.showAtLocation(btn_4,Gravity.BOTTOM,0,0)
            }

        }
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, PopupWindowAndDialogActivity::class.java)
            context.startActivity(intent)
        }
    }
}