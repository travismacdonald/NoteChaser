package com.cannonballapps.notechaser.common

sealed interface ResultOf<out T> {
    data class Success<T>(val value: T): ResultOf<T>
    data class Failure(val throwable: Throwable): ResultOf<Nothing>
}

suspend fun <T> runCatchingToResultOfSuspending(body: suspend () -> T): ResultOf<T> {
    val result = runCatching {
        body()
    }

    return if (result.isSuccess) {
        ResultOf.Success(result.getOrNull()!!)
    } else {
        ResultOf.Failure(result.exceptionOrNull()!!)
    }
}

fun <T> runCatchingToResultOf(body: () -> T): ResultOf<T> {
    val result = runCatching {
        body()
    }

    return if (result.isSuccess) {
        ResultOf.Success(result.getOrNull()!!)
    } else {
        ResultOf.Failure(result.exceptionOrNull()!!)
    }
}
