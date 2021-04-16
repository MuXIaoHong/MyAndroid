package com.nan.myandroid.coroutines.flow

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * 过滤
 */
@FlowPreview
fun main() = runBlocking<Unit> {
    (1..3).asFlow() // 一个请求流
        .filter {
            it == 2
        }
        .collect { response -> println(response) }
}