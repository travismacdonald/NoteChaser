package com.cannonballapps.notechaser.playablegenerator


import kotlin.IllegalStateException

// TODO: could override contains to use binary search for val, doesn't really make a performance difference
class ChordTemplate(_intervals: MutableList<Int> = arrayListOf())
    : PlayableTemplate(_intervals) {

    init {
        _intervals.sort()
    }

    override val range: Int
        get() {
            return if (_intervals.isEmpty())
                throw IllegalStateException("Cannot get range of empty PlayableTemplate.")
            // Take advantage of sorted list
            else _intervals[0] - _intervals[size - 1]
        }

    override fun addInterval(interval: Int) {
        if (_intervals.contains(interval)) {
            throw IllegalStateException("Chord template already contains interval $interval")
        }
        val insertionIx = -(_intervals.binarySearch(interval) + 1)
        _intervals.add(insertionIx, interval)
    }

    override fun addAllIntervals(vararg intervals: Int) {
        for (interval in intervals) {
            addInterval(interval)
        }
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