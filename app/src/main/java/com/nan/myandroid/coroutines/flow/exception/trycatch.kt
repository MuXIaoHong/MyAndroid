package com.nan.myandroid.coroutines.flow.exception

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


@FlowPreview
fun exFoo(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i) // 发射下一个值
    }
}

/**
 * 捕获整体异常两种方式
 */
@ExperimentalCoroutinesApi
@FlowPreview
fun main() = runBlocking<Unit> {
    /**
     * 1 可以将创建流到collect整个代码块try catch起来捕获异常
     */
    try {
        exFoo().collect { value ->
            println(value)
            check(value <= 1) { "Collected $value" }
        }
    } catch (e: Throwable) {
        println("Caught $e")
    }

    /**
     * 2 我们可以将 catch 操作符的声明性与处理所有异常的期望相结合，
     * 将 collect 操作符的代码块移动到 onEach 中，并将其放到 catch 操作符之前。
     * 收集该流必须由调用无参的 collect() 来触发
     */
    exFoo().onEach { value ->
        check(value <= 1) { "Collected $value" }
        println(value) }
        .catch { e -> println("Caught $e") }
        .onCompletion {

        }
        .collect()
}