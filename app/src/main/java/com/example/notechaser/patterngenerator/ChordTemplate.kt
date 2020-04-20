package com.example.notechaser.patterngenerator

import com.example.notechaser.patterngenerator.exceptions.EmptyTemplateException

class ChordTemplate(private val _intervals: MutableList<Int> = arrayListOf()) {

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
                throw EmptyTemplateException("Cannot get range of empty PatternTemplate.")
            else _intervals.max()!! - _intervals.min()!!
        }

    val size: Int
        get() = _intervals.size

    fun addInterval(interval: Int) {
        // TODO: add interval at properly sorted position
    }

    fun addAllIntervals(vararg intervals: Int) {
//        for (interval in intervals) {
//            addInterval(interval)
//        }
        // TODO: sort list, then add
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

    fun contains(interval: Int): Boolean {
        return _intervals.contains(interval)
    }

    /**
     * intervalsTransposed was chosen in this case because although two templates with
     * different (untransposed) intervals may seem different, they will result in the
     * same patterns. This is because only intervalsTransposed is used to construct patterns.
     */
    override fun equals(other: Any?): Boolean {
        return (other is PatternTemplate)
                && intervalsTransposed == other.intervalsTransposed
    }

    // Todo: make sure this works correctly
    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return _intervals.joinToString(separator = " ") { it.toString() }
    }

}