package com.cannonballapps.notechaser.musicutilities

import com.cannonballapps.notechaser.musicutilities.PitchClass

data class Note(val midiNumber: Int, val pitchClass: PitchClass, val octave: Int) {
    override fun toString() = "${pitchClass}${octave}"
}
