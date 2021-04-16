package com.nan.myandroid.coroutines

import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.coroutines.*

/**
 *author：93289
 *date:2020/8/5
 *dsc:
 */

fun <R,T>launchCoroutine(receiver:R,block:suspend R.()->T){
    block.startCoroutine(receiver,object :Continuation<T>{
        override val context: CoroutineContext=EmptyCoroutineContext


        override fun resumeWith(result: Result<T>) {
            result.onFailure {
                context[CoroutineExceptionHandler]?.onError(it)
            }
        }
    })

    val createCoroutine = block.createCoroutine(receiver, object : Continuation<T> {
        override val context: CoroutineContext = EmptyCoroutineContext

        override fun resumeWith(result: Result<T>) {
        }
    })
    createCoroutine.resume(Unit)

}

@RestrictsSuspension
class ProducerScope<T>{
   suspend fun produce(value:T){
        println(value)
    }
}

suspend fun suspendFunc02(a: String, b: String) = suspendCoroutine<Int> { continuation ->
    thread {
        continuation.resumeWith(Result.success(5)) // ... ①
    }
}

class CoroutineName(val name: String): AbstractCoroutineContextElement(Key) {
    companion object Key: CoroutineContext.Key<CoroutineName>
}

class CoroutineExceptionHandler(val onErrorAction: (Throwable) -> Unit)
    : AbstractCoroutineContextElement(Key) {
    companion object Key: CoroutineContext.Key<CoroutineExceptionHandler>

    fun onError(error: Throwable) {
        error.printStackTrace()
        onErrorAction(error)
    }
}

class LogInterceptor : ContinuationInterceptor {
    override val key = ContinuationInterceptor

    override fun <T> interceptContinuation(continuation: Continuation<T>)
            = LogContinuation(continuation)
}

class LogContinuation<T>(private val continuation: Continuation<T>)
    : Continuation<T> by continuation {
    override fun resumeWith(result: Result<T>) {
        println("before resumeWith: $result")
        continuation.resumeWith(result)
        println("after resumeWith.")
    }


}

 fun main() = runBlocking{
    launchCoroutine(ProducerScope<Int>()){
        produce(100)
//        delay(1000L)
        produce(100)
    }
}