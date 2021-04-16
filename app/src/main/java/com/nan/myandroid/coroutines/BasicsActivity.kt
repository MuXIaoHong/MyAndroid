package com.nan.myandroid.coroutines

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nan.myandroid.R
import kotlinx.coroutines.*

class BasicsActivity : AppCompatActivity() {
    val TAG = "CoroutinesBasics"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basics)

    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, BasicsActivity::class.java)
            context.startActivity(intent)
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_one -> {
                lifecycleScope.launch {
                    basicOne()
                    Thread.sleep(3000L)
                    Log.e(TAG, "8")
                }

            }
            R.id.btn_two -> {
                basicTwo()
            }

            R.id.btn_three -> {
                basicThree()
            }
            else -> {
            }
        }


    }


    /**runBlocking创建的协程会阻塞当前线程,保证该协程按该线程的代码顺序执行(阻塞)
     * 其他的协程构建器在主线程使用Dispatchers.Main构建协程时,不会阻塞该程所在主线程,而是在
     * 主线程其他非协程代码都执行完毕之后,该协程才会执行.
     * 其他的协程构建器使用非Dispatchers.Main构建协程时,会构建一个运行在子线程的协程.
     *
     */
    private suspend fun basicOne() {
        GlobalScope.launch {
            delay(1000L)
            Log.e(TAG, "0")
        }

        GlobalScope.launch(Dispatchers.Main) {
            Thread.sleep(1000L)
            Log.e(TAG, "1")
        }

        val async = GlobalScope.async {
            delay(5000L)
            9
        }
        GlobalScope.launch(Dispatchers.Main) {
            Log.e(TAG, "2")
        }
        runBlocking {
            GlobalScope.launch(Dispatchers.Main) {
                Thread.sleep(1000L)
                Log.e(TAG, "3")
            }
            GlobalScope.launch {
                Log.e(TAG, "4")
            }
        }
        Log.e(TAG, "5")
        Thread.sleep(3000L)
        Log.e(TAG, async.await().toString())
        Log.e(TAG, "6")
        Thread.sleep(3000L)
        Log.e(TAG, "7")

    }

    /**
     * 在协程中开启协程之后,父协程的代码会继续向下执行,要想等待子协程执行完父协程再执行,需要拿到子协程job引用并调用job.join
     */
    private fun basicTwo() {
        GlobalScope.launch {

            val job = GlobalScope.launch {
                delay(1000L)
                Log.e(TAG, "World")
            }
            job.join()
            Log.e(TAG, "Hello")
        }

    }


    /**
     * 结构化并发:如果我们使用GlobalScope.launch创建协程,会创建一个顶层协程,如果不显示的持有协程的job就无法控制它
     * 但是我们再父协程中使用launch{}启动一个协程的话,新协程是再父协程的作用域李的,这样就不用显示的持有子协程job,父
     * 协程取消的时候,子协程就会取消,等子协程都执行完毕了,父协程才会结束
     */
    private fun basicThree() {
        runBlocking {
            // this: CoroutineScope
            launch {
                // 在 runBlocking 作用域中启动一个新协程
                delay(1000L)
                println("World!")
            }
            println("Hello,")
        }

    }

    /**
     * coroutineScope:会创建一个协程作用域,并不是创建一个协程,它本身是一个挂起函数
     * 挂起函数:运行在协程作用域中,挂起函数执行完毕之后,才会执行协程后边的代码,用来实现阻塞式代码完成非阻塞效果的关键
     */
    private fun basicFour() = runBlocking {
        // this: CoroutineScope
        val launch = launch {
            delay(200L)
            println("Task from runBlocking")
        }

        val k9i: Int = coroutineScope {
            // 创建一个协程作用域
            launch {
                delay(500L)
                println("Task from nested launch")
            }

            delay(100L)
            println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
            1
        }

        println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
    }

}

