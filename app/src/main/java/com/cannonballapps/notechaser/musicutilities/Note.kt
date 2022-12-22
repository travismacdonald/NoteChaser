package com.cannonballapps.notechaser.musicutilities

import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils.MAX_MIDI_NUMBER
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils.MAX_OCTAVE
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils.MAX_PITCH_CLASS
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils.MIN_MIDI_NUMBER
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils.MIN_OCTAVE
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils.MIN_PITCH_CLASS
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils.isValidMidiNumber
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils.midiNumberToOctave
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils.midiNumberToPitchClass
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils.pitchClassAndOctaveToMidiNumber

@Suppress("DataClassPrivateConstructor")
data class Note private constructor(
    val midiNumber: Int,
    val pitchClass: PitchClass,
    val octave: Int,
) : Comparable<Note> {

    /**
     * Constructor based on midi number.
     */
    constructor(midiNumber: Int) : this(
        midiNumber,
        midiNumberToPitchClass(midiNumber),
        midiNumberToOctave(midiNumber),
    ) {
        assertValidMidiNumber(midiNumber)
    }

    /**
     * Constructor based on pitch class and octave.
     */
    constructor(pitchClass: PitchClass, octave: Int) : this(
        pitchClassAndOctaveToMidiNumber(pitchClass, octave),
        pitchClass,
        octave,
    ) {
        assertValidPitchClassAndOctave(pitchClass, octave)
    }

    override fun compareTo(other: Note): Int {
        return this.midiNumber.compareTo(other.midiNumber)
    }

    override fun toString() = "${pitchClass}$octave"
}

private fun assertValidMidiNumber(midiNumber: Int) {
    if (!isValidMidiNumber(midiNumber)) {
        throw IllegalArgumentException(
            "Midi number values must be between $MIN_MIDI_NUMBER and $MAX_MIDI_NUMBER.",
        )
    }
}

private fun assertValidPitchClassAndOctave(pitchClass: PitchClass, octave: Int) {
    val midiNumber = pitchClassAndOctaveToMidiNumber(pitchClass, octave)

    if (!isValidMidiNumber(midiNumber)) {
        throw IllegalArgumentException(
            "Pitch class and octave must be between ${MIN_PITCH_CLASS}$MIN_OCTAVE and ${MAX_PITCH_CLASS}$MAX_OCTAVE.",
        )
    }
}
