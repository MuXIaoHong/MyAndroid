package com.nan.myandroid.databinding

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.nan.myandroid.R
import kotlinx.android.synthetic.main.activity_databinding.*

class DataBindingActivity : AppCompatActivity() {
    val viewmode by viewModels<MyViewModel>()


    var twoWayRwAdapter :TwoWayRwAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityDatabindingBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_databinding
        )


//        <!--        普通String类型ObservableField的双向绑定-->
        binding.twoWay = viewmode.two_way_text
        et_two_way.run {
            setOnClickListener {
                viewmode.two_way_text.set("click")
            }
        }
        btn_check_value.run {
            setOnClickListener {
                text = viewmode.two_way_text.get()
            }
        }
//        <!--        普通String类型MutableLiveData的双向绑定-->


//        <!--        普通String类型ObservableField的双向绑定-->
//要指定binding的lifecycleOwner为当前界面，否则liveData更改无法通知观察者更新
        binding.lifecycleOwner = this
        binding.twoWayLivedata = viewmode.two_way_text_livedata
        et_two_way_livedata.run {
            setOnClickListener {
                viewmode.two_way_text_livedata.postValue("click")
            }
        }
        btn_check_value_livedata.run {
            setOnClickListener {
                text = viewmode.two_way_text_livedata.value
            }
        }
//        <!--        普通String类型MutableLiveData的双向绑定-->

//自定义ViewGroup内部包含xml布局使用MutableLiveData的双向绑定
        myview.initData(viewmode.two_way_text_livedata)
//自定义ViewGroup内部包含xml布局使用MutableLiveData的双向绑定

//ViewModel定义实现Observable实现双向绑定
        binding.vm = viewmode
        btn_check_value_viewmodel.setOnClickListener {
            viewmode.vmObservableField = "更改之后"
            viewmode.user = User("更改之后")
            viewmode.users = mutableListOf(User("更改之后"))
            viewmode.getRvUsersData()
        }
//ViewModel定义实现Observable实现双向绑定


//DataBinding 对RecyclerView的双向绑定
        viewmode.getRvUsersData().observe(this, Observer {
            if (twoWayRwAdapter==null){
                twoWayRwAdapter = TwoWayRwAdapter(it, this)
                rv.layoutManager=LinearLayoutManager(this)
                rv.adapter=twoWayRwAdapter
            }else{
                twoWayRwAdapter!!.setData(it)
            }
        })

    }
//DataBinding 对RecyclerView的双向绑定

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, DataBindingActivity::class.java)
            context.startActivity(intent)
        }
    }
}