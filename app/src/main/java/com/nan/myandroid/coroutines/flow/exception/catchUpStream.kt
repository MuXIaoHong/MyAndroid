package com.nan.myandroid.coroutines.flow.exception

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun foo(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i)
    }
}

/**
 * 使用catch操作符捕获上游的异常,这样捕获不到下游异常
 */
@ExperimentalCoroutinesApi
fun main() = runBlocking<Unit> {
    foo().catch { e -> println("Caught $e") } // 不会捕获下游异常
        .collect { value ->
            check(value <= 1) { "Collected $value" }
            println(value)
        }
}