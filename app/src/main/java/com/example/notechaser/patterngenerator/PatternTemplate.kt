package com.example.notechaser.patterngenerator

import java.io.Serializable

/**
 * Class that holds untransposed patterns.
 */
// todo: change serializable to parcelable
class PatternTemplate(var intervals: MutableList<Int> = arrayListOf()) : Serializable {

    var range = -1
        get() {
            if (intervals.isEmpty()) -1
            // Todo: better way of writing this?
            return (intervals.minBy { it } ?: 0) - (intervals.maxBy { it } ?: 5)
        }
        private set

    var size = intervals.size
        get() = intervals.size
        private set

    fun addInterval(interval: Int) {
        intervals.add(interval)
    }

    fun removeInterval(ix: Int) {
        intervals.removeAt(ix)
    }

}