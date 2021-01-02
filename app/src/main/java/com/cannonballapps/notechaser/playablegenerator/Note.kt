package com.cannonballapps.notechaser.playablegenerator

import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils

@Deprecated("use the better Note class")
data class Note(val ix: Int) {
    val nameSharp = MusicTheoryUtils.CHROMATIC_SCALE_SHARP[ix % MusicTheoryUtils.OCTAVE_SIZE]
    val nameFlat = MusicTheoryUtils.CHROMATIC_SCALE_FLAT[ix % MusicTheoryUtils.OCTAVE_SIZE]
}