package com.example.notechaser.playablegenerator


// TODO: remove duplicated equals() and hashCode()
/**
 * Class that holds intervals for patterns templates.
 */
class PatternTemplate(_intervals: MutableList<Int> = arrayListOf())
    : PlayableTemplate(_intervals) {

    override val range: Int
        get() {
            return if (_intervals.isEmpty())
                throw IllegalStateException("Cannot get range of empty PlayableTemplate.")
            else _intervals.maxOrNull()!! - _intervals.minOrNull()!!
        }

    override fun addInterval(interval: Int) {
        _intervals.add(interval)
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