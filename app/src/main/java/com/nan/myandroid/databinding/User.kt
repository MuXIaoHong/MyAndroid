package com.nan.myandroid.databinding

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.nan.myandroid.BR

/**
 *author：93289
 *date:2020/8/25
 *dsc:
 */
data class User(var name: String)


class ObservableUser(name: String) : BaseObservable() {
    var name: String = name
        @Bindable
        get() {
            return field
        }
        set(value) {
            Log.d("ObservableUser",value)
            field = value
            notifyPropertyChanged(BR.name)
        }

}