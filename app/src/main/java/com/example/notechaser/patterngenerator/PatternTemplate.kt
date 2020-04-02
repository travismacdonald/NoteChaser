package com.example.notechaser.patterngenerator

import java.io.Serializable

/**
 * Class that holds untransposed patterns.
 */
// todo: change serializable to parcelable
class PatternTemplate(private val _intervals: MutableList<Int> = arrayListOf()) : Serializable {

    var intervals: List<Int> = _intervals
        get() = _intervals
        private set

    var range = -1
        get() {
            if (_intervals.isEmpty()) -1
            return (_intervals.minBy { it }!!) - (_intervals.maxBy { it }!!)
        }
        private set

    var size = _intervals.size
        get() = _intervals.size
        private set

    fun addInterval(interval: Int) {
        _intervals.add(interval)
    }

    fun removeInterval(ix: Int) {
        _intervals.removeAt(ix)
    }

}