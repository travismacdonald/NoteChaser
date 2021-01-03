package com.cannonballapps.notechaser.musicutilities

data class Note(
        val midiNumber: Int,
        val pitchClass: PitchClass,
        val octave: Int
) : Comparable<Note> {

    override fun compareTo(other: Note): Int {
        return this.midiNumber.compareTo(other.midiNumber)
    }

    override fun toString() = "${pitchClass}${octave}"


}
