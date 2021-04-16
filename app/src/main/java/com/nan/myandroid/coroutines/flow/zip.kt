package com.nan.myandroid.coroutines.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis


/**
 *将两个事件异步的执行，并将结果合并
 */
@FlowPreview
fun zipFoo1(): Flow<Int> = flow {
    delay(1000)
    emit(1)
}

@FlowPreview
fun zipFoo2(): Flow<Int> = flow {
    delay(1000)
    emit(2)
}

@FlowPreview
fun main() = runBlocking<Unit> {
    val time = measureTimeMillis {
        zipFoo1().zip(zipFoo2()) { a, b ->
            println(a + b)
            "呵呵"
        }.collect{
            println(it)
        }
    }
    println(time)

}