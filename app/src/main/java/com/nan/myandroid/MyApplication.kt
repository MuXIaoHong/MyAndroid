package com.nan.myandroid

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
//        cactus {
//            setChannelId("测试保活")
//            setTitle("后台运行")
//            setSmallIcon(R.mipmap.ic_launcher)
//            setMusicId(R.raw.fairytale)
//            isDebug(true)
//            addBackgroundCallback {
//                Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
//            }
//        }

    }

}