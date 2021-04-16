package com.nan.myandroid.transition_animation.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.nan.myandroid.R

class PreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_over_trasition)
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, PreActivity::class.java)
            context.startActivity(intent)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_finish -> {
                finish()
                //退出Activity时设置过渡动画只有一种情况
                //在finish Activity后调用overridePendingTransition，传入两个动画的Resource Id
                //R.anim.activity_enter是当前Activity退出后要显示的Activity的进场动画
                //R.anim.activity_exit是当前Activity退出的时候使用的动画
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit)
            }
            R.id.btn_start_01->{
                //跳转其他Activity时设置过渡动画有两种情况
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
                    //SDK Version 小于 16时,使用startActivity后调用overridePendingTransition，传入两个动画的Resource Id
                    //R.anim.activity_enter是startActivity的目标Activity的进场动画
                    //R.anim.activity_exit是当前Activity退出的时候使用的动画
                    startActivity(Intent(this,AfterActivity::class.java))
                    overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit)
                }else{
                    //SDK Version 大于等于16时,使用startActivity(Intent intent, @Nullable Bundle options)
                    //其中options通过ActivityOptionsCompat makeCustomAnimation(@NonNull Context context,int enterResId, int exitResId)得到
                    //R.anim.activity_enter是startActivity的目标Activity的进场动画
                    //R.anim.activity_exit是当前Activity退出的时候使用的动画
                    val optionsBundle = ActivityOptionsCompat.makeCustomAnimation(
                        this,
                        R.anim.activity_enter,
                        R.anim.activity_exit
                    ).toBundle()
                    startActivity(Intent(this,AfterActivity::class.java),optionsBundle)
                }
            }
        }
    }
}