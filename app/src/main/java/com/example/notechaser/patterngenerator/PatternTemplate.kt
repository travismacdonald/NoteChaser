package com.example.notechaser.patterngenerator

import java.io.Serializable

/**
 * Class that holds intervals for patterns templates.
 */
// todo: change serializable to parcelable
class PatternTemplate(private val _intervals: MutableList<Int> = arrayListOf()) : Serializable {

    /**
     * Intervals that are unmodified. (Backing property for _intervals)
     */
    val intervals: List<Int>
        get() = _intervals

    /**
     * Intervals that are all equally transposed so that the minimum interval is zero.
     */
    val intervalsTransposed: List<Int>
        get() {
            return if (_intervals.min() == 0) _intervals
            else _intervals.map { it - _intervals.min()!! }
        }

    val range: Int
        get() {
            return if (_intervals.isEmpty())
                throw EmptyPatternTemplateException("Cannot get range of empty PatternTemplate.")
            else _intervals.max()!! - _intervals.min()!!
        }

    var size = _intervals.size
        get() = _intervals.size
        private set

    fun addInterval(interval: Int) {
        _intervals.add(interval)
    }

    fun addAllIntervals(vararg intervals: Int) {
        for (interval in intervals) {
            addInterval(interval)
        }
    }

    fun removeIntervalAt(ix: Int) {
        _intervals.removeAt(ix)
    }

    fun isEmpty(): Boolean {
        return _intervals.isEmpty()
    }

    fun isNotEmpty(): Boolean {
        return _intervals.isNotEmpty()
    }

    override fun equals(other: Any?): Boolean {
        return (other is PatternTemplate)
                && _intervals == other._intervals
    }

    // Todo: make sure this works correctly
    override fun hashCode(): Int {
        return super.hashCode()
    }

}

class EmptyPatternTemplateException(message: String) : Exception()