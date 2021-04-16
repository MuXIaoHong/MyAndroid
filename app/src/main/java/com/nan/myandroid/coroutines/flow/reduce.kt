package com.nan.myandroid.coroutines.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 归纳
 * reduce两个参数，第一个是积聚者，第二个是上游的事件你
 * lambda打返回值会赋给积聚者并用于下个事件
 */
@FlowPreview
fun main() = runBlocking<Unit> {

    val sum = (1..5).asFlow()
        .reduce { a, b -> a + b } // 求和（末端操作符）
    println(sum) }