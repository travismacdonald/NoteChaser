package com.cannonballapps.notechaser.musicutilities

sealed class ParentScale(
    val intervals: List<Int>,
    val names: List<String>,
) {

    object Major : ParentScale(
        intervals = MusicTheoryUtils.MAJOR_SCALE_INTERVALS.toList(),
        names = MusicTheoryUtils.MAJOR_MODE_NAMES.toList(),
    )

    object MelodicMinor : ParentScale(
        intervals = MusicTheoryUtils.MELODIC_MINOR_SCALE_INTERVALS.toList(),
        names = MusicTheoryUtils.MELODIC_MINOR_MODE_NAMES.toList(),
    )

    object HarmonicMinor : ParentScale(
        intervals = MusicTheoryUtils.HARMONIC_MINOR_SCALE_INTERVALS.toList(),
        names = MusicTheoryUtils.HARMONIC_MINOR_MODE_NAMES.toList(),
    )

    object HarmonicMajor : ParentScale(
        intervals = MusicTheoryUtils.HARMONIC_MAJOR_SCALE_INTERVALS.toList(),
        names = MusicTheoryUtils.HARMONIC_MAJOR_MODE_NAMES.toList(),
    )

    fun scaleAtIndex(index: Int) = scales[index]

    val scales = listOf(
        createScaleAtIndex(0),
        createScaleAtIndex(1),
        createScaleAtIndex(2),
        createScaleAtIndex(3),
        createScaleAtIndex(4),
        createScaleAtIndex(5),
        createScaleAtIndex(6),
    )

    private fun createScaleAtIndex(index: Int): Scale =
        Scale(
            intervals = MusicTheoryUtils.getIntervalsForModeAtIx(this.intervals.toIntArray(), index).toList(),
            parentScale = this,
            modeIndex = index,
            name = this.names.get(index),
        )
}

// todo shouldn't let this constructor be called
data class Scale internal constructor(
    val intervals: List<Int>,
    val parentScale: ParentScale,
    val modeIndex: Int,
    val name: String,
)
