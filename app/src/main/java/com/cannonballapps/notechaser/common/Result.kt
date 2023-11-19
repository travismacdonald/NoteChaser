package com.cannonballapps.notechaser.common

sealed interface ResultOf<out T> {
    data class Success<T>(val value: T): ResultOf<T>
    data class Failure(val throwable: Throwable): ResultOf<Nothing>
}
