package com.example.notechaser.patterngenerator

import java.io.Serializable

/**
 * Class that holds untransposed patterns.
 * List given will be transformed so that the lowest int is zero.
 */
// todo: change serializable to parcelable
class PatternTemplate(private val _intervals: MutableList<Int> = arrayListOf()) : Serializable {

    init {
        if (_intervals.isNotEmpty() && _intervals.min() != 0) {
            val min = _intervals.min()!!
            for (i in 0 until _intervals.size) {
                _intervals[i] = _intervals[i] - min
            }
        }
    }

    var intervals: List<Int> = _intervals
        get() = _intervals
        private set

    var range = -1
        get() {
            if (_intervals.isEmpty()) return -1
            else return _intervals.max()!!
        }
        private set

    var size = _intervals.size
        get() = _intervals.size
        private set

    fun addInterval(interval: Int) {
        _intervals.add(interval)
    }

    fun removeIntervalAt(ix: Int) {
        _intervals.removeAt(ix)
    }

}