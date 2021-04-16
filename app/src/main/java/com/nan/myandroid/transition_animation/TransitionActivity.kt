package com.nan.myandroid.transition_animation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nan.myandroid.R
import com.nan.myandroid.transition_animation.activity.PreActivity

class TransitionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, TransitionActivity::class.java)
            context.startActivity(intent)
        }
    }

    fun onClick(view: View) {
        when(view.id){
            R.id.btn_activity->{
                PreActivity.launch(this)
            }
        }

    }

}