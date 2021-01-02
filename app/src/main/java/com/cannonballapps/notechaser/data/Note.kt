package com.cannonballapps.notechaser.data

import com.cannonballapps.notechaser.utilities.PitchClass

data class Note(val midiNumber: Int, val pitchClass: PitchClass, val octave: Int) {
    override fun toString() = "${pitchClass}${octave}"
}
