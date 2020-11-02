package com.example.notechaser.playablegenerator

import com.example.notechaser.utilities.MusicTheoryUtils

@Deprecated("fuck off: use `Int` instead")
data class Note(val ix: Int) {
    val nameSharp = MusicTheoryUtils.CHROMATIC_SCALE_SHARP[ix % MusicTheoryUtils.OCTAVE_SIZE]
    val nameFlat = MusicTheoryUtils.CHROMATIC_SCALE_FLAT[ix % MusicTheoryUtils.OCTAVE_SIZE]
}