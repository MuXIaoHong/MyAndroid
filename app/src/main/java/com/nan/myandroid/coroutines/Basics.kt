package com.nan.myandroid.coroutines

import kotlinx.coroutines.*
import kotlin.concurrent.thread

//基础的示例运行在JVM上，用于与Android的Main线程对比
fun main() {
//    basicOne()
    basicTwo()
}

/**
 * 在JVM main中，使用GlobalScope.launch启动一个携程，默认运行在子线程中
 * 如果不让主线程sleep2000，携程中的代码将执行不到，因为主线程在走完main方法之后就结束了进程，
 * 所以如果使用GlobalScope.launch等阻塞开启协程的方法时要保证主线程存活（在Android中使用的时候不用，
 * 因为Android中MainThread一直都在运行着，进程不会结束）
 * 使用Tread的替代GlobalScope.launch做同样的操作时，主线程走完main方法之后会等待子线程执行完毕，
 * 这是线程与携程很大的不同。
 */
fun basicOne() {
    val job = GlobalScope.launch {
        // 在后台启动一个新的协程并继续
        delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
        println(Thread.currentThread().name + "World!") // 在延迟后打印输出
    }
//    thread{
//        Thread.sleep(1000L)
//        println("World!") // 在延迟后打印输出
//    }

    println("Hello,") // 协程已在等待时主线程还在继续
    Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
}

/**
 * 使用GlobalScope.launch构建的协程是非阻塞式的（运行在子线程）
 * 使用runBlocking构建的协程时阻塞式的（运行在主线程），可以保证JVM不结束进程
 */
fun basicTwo() {
    runBlocking {
        GlobalScope.launch {
            // 在后台启动一个新的协程并继续
//            delay(1000L)
            Thread.sleep(1000L)
            println("World!")
        }
//        runBlocking {
//            //                         在后台启动一个新的协程并继续
//            println("World!")
//        }

        println("Hello,") // 主协程在这里会立即执行
//        delay(2000L)      // 延迟 2 秒来保证 JVM 存活
    }
}

/**
 * 全局协程像守护线程,在 GlobalScope 中启动的活动协程并不会使进程保活。它们就像守护线程
 */
fun basicThree() = runBlocking {
    GlobalScope.launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // 在延迟后退出
}
