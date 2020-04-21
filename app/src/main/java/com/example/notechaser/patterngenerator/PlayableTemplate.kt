package com.example.notechaser.patterngenerator


// TODO: probably want to implement a max size at some point
abstract class PlayableTemplate(protected val _intervals: MutableList<Int>) {

    /**
     * Intervals that are untransposed.
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

    abstract val range: Int

    val size: Int
        get() = _intervals.size

    abstract fun addInterval(interval: Int)

    abstract fun addAllIntervals(vararg intervals: Int)

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