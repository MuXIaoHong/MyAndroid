package com.nan.myandroid.databinding

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nan.myandroid.BR
import kotlinx.coroutines.delay
import kotlin.concurrent.thread

/**
 *author：93289
 *date:2020/8/24
 *dsc:
 */
class MyViewModel : ViewModel(), Observable {

    //普通String类型ObservableField的双向绑定
    var two_way_text: ObservableField<String> = ObservableField("init")
        get() {
            field.set("${field.get()} 重写get方法")
            return field
        }
        set(value) {
            value.set(value.get() + "重写set方法无效")
            field = value
        }
    //普通String类型ObservableField的双向绑定


    //String类型MutableLiveData的双向绑定
    val two_way_text_livedata by lazy {
        MutableLiveData("init")
    }

    val two_way_user_livedata by lazy {
        MutableLiveData(User("刘大哥"))
    }

    //String类型MutableLiveData的双向绑定


    //ViewModel 实现Observable接口使用普通数据的双向绑定
    @get:Bindable
    var vmObservableField = "init"
        set(value) {
            field = value
            thread {
                //退出Activity之后ViewModel调用onCleared，但是这里延时结束之后会继续执行notifyPropertyChanged，但是并没有内存泄漏。
                // 这里没有看具体原因：推测可能是ViewModel没有之间或间接持有Activity引用或者是jetpack帮我们退出Activity时清理掉了
                Thread.sleep(5000)
                Log.d("MyViewModel", "延时结束")
                notifyPropertyChanged(BR.vmObservableField)
            }
        }

    @get:Bindable
    var user = User("刘大哥")
        set(value) {
            field = value

            notifyPropertyChanged(BR.user)
        }

    @get:Bindable
    var users = mutableListOf(User("刘大哥列表"))
        set(value) {
            field = value
            notifyPropertyChanged(BR.users)
        }


    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback
    ) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback
    ) {
        callbacks.remove(callback)
    }


    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }


    fun notifyPropertyChanged(fieldId: Int) {
        Log.d("MyViewModel", "notifyPropertyChanged")
        callbacks.notifyCallbacks(this, fieldId, null)
    }
    //ViewModel 实现Observable接口使用普通数据的双向绑定


    //DataBinding 对RecyclerView的双向绑定,我使用LiveData通知RecyclerView的Adapter更新界面中的ObservableUser数据
    //反更新的话界面中绑定的每个ObservableUser会在@={}表达式绑定的属性更新
    //双向绑定的意义在于更新界面的同时更新数据源，使用的时间就是你觉得你对界面的直接改动需要更新数据源时，如果有些操作明显是在对数
    // 据源操作，那就直接操作数据源就可以了。
    //双向绑定只有在必要的时候才建议使用：例如一个操作直接改变了界面显示，数据源需要同步时。
    private var rvUsers = mutableListOf(ObservableUser("name01"))
    private var rvUsersLiveData = MutableLiveData<MutableList<ObservableUser>>()

    fun getRvUsersData(): MutableLiveData<MutableList<ObservableUser>> {
        rvUsers.add(ObservableUser("name02"))
        rvUsersLiveData.postValue(rvUsers)
        return rvUsersLiveData
    }
    //DataBinding 对RecyclerView的双向绑定




    override fun onCleared() {
        super.onCleared()
        Log.d("MyViewModel", "onCleared")
    }

}