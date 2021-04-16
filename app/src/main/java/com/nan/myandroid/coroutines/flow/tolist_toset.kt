package com.nan.myandroid.coroutines.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@FlowPreview
fun main() = runBlocking<Unit> {

    val list = (1..5).asFlow()
        .toList()
    println(list)

    val sum = (1..5).asFlow()
        .toSet()
    println(sum)
}