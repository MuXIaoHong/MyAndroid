package com.nan.myandroid.coroutines.flow

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking

/**
 *转换
 *可以将上游的事件做一下处理
 *并且在上游事件前后加上一些事件
 * 与map{ @link}的不同就是在transform里可以emit事件
 */
@FlowPreview
fun main() = runBlocking<Unit> {
    (1..3).asFlow() // 一个请求流
        .transform { request ->
            emit("Making request $request")
            emit(performRequest(request))
        }
        .collect { response -> println(response) }
}