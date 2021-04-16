package com.nan.myandroid.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.storage.StorageManager
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.nan.myandroid.R
import com.nan.myandroid.rengwuxian.view.CustomViewActivity
import kotlinx.android.synthetic.main.activity_view_model.*

class ViewModelActivity : AppCompatActivity() {
    //implementation "androidx.activity:activity-ktx:1.1.0"  使用ktx创建ViewModel实例
    //val viewModel by viewModels<ActivityViewModel>()

    //使用默认Factory创建ViewModel实例
    val viewModel by lazy {
    ViewModelProvider(this).get(ActivityViewModel::class.java)
     }

 /*   使用自定义Factory创建ViewModel实例（当ViewModel需要通过构造函数传入值时才需要这么做）
    val viewModel by lazy {
        ViewModelProvider(this, MyViewModelFactory(1)).get(ActivityViewModel::class.java)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model)
        tv_test.text = viewModel.getTestText()
        btn_change_text.setOnClickListener {
            viewModel.changeTestText()
            tv_test.text = viewModel.getTestText()
        }
        img_test.setImageBitmap(viewModel.getTestBitmap())
        btn_change_image.setOnClickListener {
            viewModel.changeTestBitmap()
            img_test.setImageBitmap(viewModel.getTestBitmap())
        }
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, ViewModelActivity::class.java)
            context.startActivity(intent)
        }
    }
}

