package com.nan.myandroid.databinding

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.MutableLiveData
import com.nan.myandroid.Utils.dp

/**
 *author：93289
 *date:2020/8/25
 *dsc:
 */
class MyView02(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = TextPaint().apply {
        textSize = 20.dp
        color = Color.parseColor("#f4f422")
    }

    //个人觉得LiveData不适合用来做自定义View的双向绑定
    var userLiveData=MutableLiveData<User>(User("userLiveDataInit"))




    var data: String="MyView02"
    set(value) {
        field=value
        invalidate()
    }

    var user: User=User("UserInit")
        set(value) {
            field=value
            invalidate()
        }

    var users: MutableList<User> = mutableListOf(User("UsersInit"))
        set(value) {
            field=value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText("${user.name}: $data  ${users[0].name} ${userLiveData.value?.name}" , 0f, height.toFloat(), paint)
    }


    companion object {
        //自定义View属性实现双向绑定步骤
        //一、定义属性设置在xml中的表达式
        @BindingAdapter("data")
        @JvmStatic
        fun setData(view: MyView02, newData: String) {
            Log.d("MyView02",newData)
            if (view.data!=newData)
            view.data = newData
        }

        //二、定义属性在反向改变的时候的取值方法
        @InverseBindingAdapter(attribute = "data")
        @JvmStatic
        fun getData(view: MyView02):String {
            return view.data
        }

        //二、定义反向更新的时机，需要强制包含后缀 "xxxAttrChanged",xxx一般为属性名称
        @BindingAdapter("app:dataAttrChanged")
        @JvmStatic fun setDataListener(
            view: MyView02,
            attrChange: InverseBindingListener
        ) {
            // Set a listener for click, focus, touch, etc.
             with (view){
                 setOnClickListener {
                     view.data="click"
                     attrChange.onChange()
                 }
             }

        }


        @BindingAdapter("user")
        @JvmStatic
        fun setUser(view: MyView02, newUser: User) {
            if (view.user!=newUser)
                view.user = newUser
        }

        @InverseBindingAdapter(attribute = "user")
        @JvmStatic
        fun getUser(view: MyView02):User {
            return view.user
        }

        //强制包含后缀 "AttrChanged"
        @BindingAdapter("app:userAttrChanged")
        @JvmStatic fun setUserListener(
            view: MyView02,
            attrChange: InverseBindingListener
        ) {
            // Set a listener for click, focus, touch, etc.
            with (view){
                setOnLongClickListener {
                    view.user=User("clickUser")
                    attrChange.onChange()
                    return@setOnLongClickListener true
                }
            }

        }

        @BindingAdapter("users")
        @JvmStatic
        fun setUsers(view: MyView02, newUsers: MutableList<User>) {
            if (view.users!=newUsers)
                view.users = newUsers
        }

        @InverseBindingAdapter(attribute = "users")
        @JvmStatic
        fun getUsers(view: MyView02):MutableList<User> {
            return view.users
        }

        //强制包含后缀 "AttrChanged"
        @BindingAdapter("app:usersAttrChanged")
        @JvmStatic fun setUsersListener(
            view: MyView02,
            attrChange: InverseBindingListener
        ) {
            // Set a listener for click, focus, touch, etc.
            with (view){
                setOnTouchListener { _, _ ->
                    view.users= mutableListOf(User("clickUsers"))
                    attrChange.onChange()
                    return@setOnTouchListener false
                }
            }

        }
    }
}

