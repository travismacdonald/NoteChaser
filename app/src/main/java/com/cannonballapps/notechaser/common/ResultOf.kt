package com.cannonballapps.notechaser.common

sealed interface ResultOf<out T> {
    data class Success<T>(val value: T): ResultOf<T>
    data class Failure(val throwable: Throwable): ResultOf<Nothing>
}

suspend fun <T> runCatchingToResultOfSuspending(body: suspend () -> T): ResultOf<T> =
    try {
        ResultOf.Success(body())
    } catch (e: Exception) {
        ResultOf.Failure(e)
    }

fun <T> runCatchingToResultOf(body: () -> T): ResultOf<T> =
    try {
        ResultOf.Success(body())
    } catch (e: Exception) {
        ResultOf.Failure(e)
    }
