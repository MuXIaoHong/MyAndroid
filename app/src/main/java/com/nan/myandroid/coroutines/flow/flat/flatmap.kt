package com.nan.myandroid.coroutines.flow.flat

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


@FlowPreview
fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500) // 等待 500 毫秒
    emit("$i: Second")
}

@FlowPreview
fun main() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis() // 记录开始时间

    /**
     * flatMapConcat
     *requestFlow函数将上游的每个事件又转换成了新的flow了，那么如果不做处理，collect接受到的就是
     * 一个个flow，flatMapConcat的作用就是转换并且将转换成的每个新的flow的事件合兵成一个事件流
     * 这样collect接受到的事件就是转换合并之后的
     */
    (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
        .flatMapConcat { requestFlow(it) }
        .collect { value ->
            // 收集并打印
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }

    /**
     * flatMapMerge
     * 与flatMapConcat区别就是转换成的新的flow中的事件会并发的合并，不是按执行顺序，具体看示例体会其区别
     */
    (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
        .flatMapMerge { requestFlow(it) }
        .collect { value ->
            // 收集并打印
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }

}