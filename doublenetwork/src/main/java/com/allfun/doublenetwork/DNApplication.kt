package com.allfun.doublenetwork

import android.app.Application
import android.content.Context
import com.arashivision.sdkcamera.InstaCameraSDK

 class DNApplication : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        InstaCameraSDK.init(this)
    }

}