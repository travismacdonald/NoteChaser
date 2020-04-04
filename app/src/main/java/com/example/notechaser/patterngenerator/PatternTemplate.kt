package com.example.notechaser.patterngenerator

import java.io.Serializable

/**
 * Class that holds intervals for patterns templates.
 */
// todo: change serializable to parcelable
class PatternTemplate(private val _intervals: MutableList<Int> = arrayListOf()) : Serializable {

    /**
     * Intervals are unmodified.
     */
    var intervalsUntransposed: List<Int> = _intervals
        get() = _intervals
        private set

    /**
     * Intervals that are transposed so that the minimum interval is zero.
     */
    var intervalsTransposed: List<Int> = _intervals
        get() {
            if (_intervals.min() == 0) return _intervals
            else return _intervals.map { it - _intervals.min()!! }
        }
        private set

    var range = -1
        get() {
            if (_intervals.isEmpty()) return -1
            else return _intervals.max()!! - _intervals.min()!!
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