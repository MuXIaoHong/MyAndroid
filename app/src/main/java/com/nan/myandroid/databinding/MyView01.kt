package com.nan.myandroid.databinding

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.nan.myandroid.R

/**
 *author：93289
 *date:2020/8/25
 *dsc:
 */
class MyView01 : LinearLayout {
    lateinit var binding: CustomViewInsideLayoutBinding

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        binding= DataBindingUtil.inflate<CustomViewInsideLayoutBinding>(
            LayoutInflater.from(context),
            R.layout.custom_view_inside_layout,
            null,
            false
        )
        this.addView(binding.root)

    }

    fun initData(twoWayTextLivedata: MutableLiveData<String>) {
        binding.lifecycleOwner=context as DataBindingActivity
        binding.twoWayLivedata=twoWayTextLivedata
    }


}