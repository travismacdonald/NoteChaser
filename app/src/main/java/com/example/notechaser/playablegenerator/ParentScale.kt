package com.example.notechaser.playablegenerator

data class ParentScale(
        val name: String,
        val intervals: IntArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParentScale

        if (name != other.name) return false
        if (!intervals.contentEquals(other.intervals)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + intervals.contentHashCode()
        return result
    }

}