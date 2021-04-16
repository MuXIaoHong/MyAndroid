package com.nan.myandroid.coroutines.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 折叠，与reduce区别就是多了一个初始值
 */
@FlowPreview
fun main() = runBlocking<Unit> {

    val sum = (1..5).asFlow()
        .fold(0 ) { a, b ->
            a + b
        }
    println(sum)
}