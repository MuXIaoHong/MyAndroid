package com.nan.myandroid.coroutines.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 *使用方法：
 *1 名为 flow 的 Flow 类型构建器函数。
 *2 flow { ... } 构建块中的代码可以挂起。
 *3 函数 foo() 不再标有 suspend 修饰符。
 *4 流使用 emit 函数 发射 值。
 *5 流使用 collect 函数 收集 值。
 */


@FlowPreview
fun foo(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100)
        println("Emitting $i")
        emit(i)
    }
}

@FlowPreview
fun main() = runBlocking {
    launch {
        for (i in 1..3) {
            delay(100) // 假装我们在这里做了一些有用的事情
            println("I am in launch") // 发送下一个值
        }
    }

    foo().collect {
        println(it)
    }

}