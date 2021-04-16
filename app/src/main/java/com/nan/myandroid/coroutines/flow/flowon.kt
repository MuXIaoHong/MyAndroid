package com.nan.myandroid.coroutines.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

/**
 * 上游线程切换使用flowOn,使用withContext会报错
 */
@FlowPreview
fun flowOnFoo(): Flow<Int> = flow {
    for (i in 1..3) {
        Thread.sleep(100) // 假装我们以消耗 CPU 的方式进行计算
        log("Emitting $i")
        emit(i) // 发射下一个值
    }
}.flowOn(Dispatchers.Default) // 在流构建器中改变消耗 CPU 代码上下文的正确方式

@FlowPreview
fun main() = runBlocking<Unit> {
    flowOnFoo().collect { value ->
        log("Collected $value")
    }
}