package com.cannonballapps.notechaser.musicutilities

import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils

enum class ParentScale2(val intervals: List<Int>, val modeNames: List<String>) {

    MAJOR(
            MusicTheoryUtils.MAJOR_SCALE_INTERVALS.toList(),
            MusicTheoryUtils.MAJOR_MODE_NAMES.toList()
    ) {
        override fun toString() = "Major"
    },

    MELODIC_MINOR(
            MusicTheoryUtils.MELODIC_MINOR_SCALE_INTERVALS.toList(),
            MusicTheoryUtils.MELODIC_MINOR_MODE_NAMES.toList()
    ) {
        override fun toString() = "Melodic Minor"
    },

    HARMONIC_MINOR(
            MusicTheoryUtils.HARMONIC_MINOR_SCALE_INTERVALS.toList(),
            MusicTheoryUtils.HARMONIC_MINOR_MODE_NAMES.toList()
    ) {
        override fun toString() = "Harmonic Minor"
    },

    HARMONIC_MAJOR(
            MusicTheoryUtils.HARMONIC_MAJOR_SCALE_INTERVALS.toList(),
            MusicTheoryUtils.HARMONIC_MAJOR_MODE_NAMES.toList()
    ) {
        override fun toString() = "Harmonic Major"
    }

}

fun ParentScale2.getModeAtIx(ix: Int): Scale {
    val name = modeNames[ix]
    val intervals = MusicTheoryUtils.getIntervalsForModeAtIx(this.intervals.toIntArray(), ix)
    return Scale(
            name,
            intervals
    )
}