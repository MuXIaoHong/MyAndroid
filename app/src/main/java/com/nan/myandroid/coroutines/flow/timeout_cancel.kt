package com.nan.myandroid.coroutines.flow

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

/**
 * 超时取消
 */


@FlowPreview
fun main() = runBlocking<Unit> {
    withTimeoutOrNull(250) {
        // 在 250 毫秒后超时
        foo().collect {
            value -> println(value)
        }
    }
    println("Done")
}