package com.cannonballapps.notechaser.data

import com.cannonballapps.notechaser.utilities.MusicTheoryUtils

enum class ParentScale2(val intervals: List<Int>, val modeNames: List<String>) {

    MAJOR(MusicTheoryUtils.MAJOR_SCALE_SEQUENCE.toList(), MusicTheoryUtils.MAJOR_MODE_NAMES.toList()) {
        override fun toString() = "Major"
    },

    MELODIC_MINOR(MusicTheoryUtils.MELODIC_MINOR_SCALE_SEQUENCE.toList(), MusicTheoryUtils.MELODIC_MINOR_MODE_NAMES.toList()) {
        override fun toString() = "Melodic Minor"
    },

    HARMONIC_MINOR(MusicTheoryUtils.HARMONIC_MINOR_SCALE_SEQUENCE.toList(), MusicTheoryUtils.HARMONIC_MAJOR_MODE_NAMES.toList()) {
        override fun toString() = "Harmonic Minor"
    },

    HARMONIC_MAJOR(MusicTheoryUtils.HARMONIC_MAJOR_SCALE_SEQUENCE.toList(), MusicTheoryUtils.HARMONIC_MAJOR_MODE_NAMES.toList()) {
        override fun toString() = "Harmonic Major"
    }

}