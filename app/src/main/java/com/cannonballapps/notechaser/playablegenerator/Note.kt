package com.cannonballapps.notechaser.playablegenerator

import com.cannonballapps.notechaser.utilities.MusicTheoryUtils

@Deprecated("fuck off: use `Int` instead")
data class Note(val ix: Int) {
    val nameSharp = MusicTheoryUtils.CHROMATIC_SCALE_SHARP[ix % MusicTheoryUtils.OCTAVE_SIZE]
    val nameFlat = MusicTheoryUtils.CHROMATIC_SCALE_FLAT[ix % MusicTheoryUtils.OCTAVE_SIZE]
}