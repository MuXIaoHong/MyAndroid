package com.nan.myandroid.coroutines.flow

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * map用于将上游的事件转换成之后传给下游
 *
 *
 */
suspend fun performRequest(request: Int): String {
    println("performRequest")
    delay(1000) // 模仿长时间运行的异步工作
    return "response $request"
}

@FlowPreview
fun main() = runBlocking<Unit> {
    (1..3).asFlow() // 一个请求流
        .map { request ->
//          map的返回值为lambda最后一行结果，这个结果将作为新事件发送个下游
            performRequest(request)
        }
        .collect { response -> println(response) }
}