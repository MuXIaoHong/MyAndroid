package com.nan.myandroid.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.*
import com.nan.myandroid.MyApplication
import com.nan.myandroid.R

/**
 *author：93289
 *date:2020/8/24
 *dsc:
 */
//想在ViewModel构造函数传入值的时候需要重写ViewModelProvider.Factory而不能直接调用ViewModel构造器
//class ActivityViewModel(private val param:Int): ViewModel() {

//需要在ViewModel中使用application对象或者applicationContext并使用默认的Factory时可以使用AndroidViewModel
class ActivityViewModel(context: Application) : AndroidViewModel(context) {


    //改变文本和bitmap之后旋转屏幕，更改之后的数据没有丢失----------
    private var testText = "textText"
    private var testBitmap =BitmapFactory.decodeResource(MyApplication.context.resources, R.drawable.avatar)

    fun getTestText(): String {
        return testText
    }


    fun changeTestText(){
        testText="changeTestText"
    }

    fun getTestBitmap():Bitmap{
        return testBitmap
    }
    fun changeTestBitmap(){
        testBitmap=BitmapFactory.decodeResource(MyApplication.context.resources, R.drawable.logo)
    }

    //改变文本和bitmap之后旋转屏幕，更改之后的数据没有丢失----------

    /**
     * 需要在ViewModel中使用application对象或者applicationContext并使用默认的Factory时可以使用AndroidViewModel
     * 使用自己定义的Factory时可以自己添加
     */
    fun userApplication(){
        getApplication<MyApplication>().applicationContext.cacheDir.path.let {
            Log.d("ActivityViewModel",it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ActivityViewModel","onCleared")
    }

}


class MyViewModelFactory(private val param:Int):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Int::class.java).newInstance(param)
    }
}