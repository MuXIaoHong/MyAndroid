package com.nan.myandroid.coroutines.flow

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking


/**
 * 流构建器
 */
@FlowPreview
fun main() = runBlocking {
    flowOf(1).collect {
        println(it)
    }
    listOf(1).asFlow().collect {
        println(it)
    }
}