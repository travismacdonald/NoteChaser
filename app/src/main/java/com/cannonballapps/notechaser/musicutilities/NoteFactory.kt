package com.cannonballapps.notechaser.musicutilities

object NoteFactory {

    fun makeNoteFromMidiNumber(midiNumber: Int): Note {
        assertValidMidiNumber(midiNumber)
        val pitchClass = getPitchClassFromMidiNumber(midiNumber)
        val octave = getOctaveFromMidiNumber(midiNumber)
        return Note(midiNumber, pitchClass, octave)
    }

    fun makeNoteFromPitchClassAndOctave(pitchClass: PitchClass, octave: Int): Note {
        val midiNumber = getMidiNumberFromPitchClassAndOctave(pitchClass, octave)
        assertValidMidiNumber(midiNumber)
        return Note(midiNumber, pitchClass, octave)
    }

    private fun getPitchClassFromMidiNumber(midiNumber: Int, preferFlat: Boolean = true): PitchClass {
        val pitchClassIx = midiNumber % MusicTheoryUtils.OCTAVE_SIZE
        return if (preferFlat)
            MusicTheoryUtils.CHROMATIC_PITCH_CLASSES_FLAT[pitchClassIx]
        else
            MusicTheoryUtils.CHROMATIC_PITCH_CLASSES_SHARP[pitchClassIx]
    }

    private fun getOctaveFromMidiNumber(midiNumber: Int): Int {
        return (midiNumber / MusicTheoryUtils.OCTAVE_SIZE) - 1
    }

    private fun getMidiNumberFromPitchClassAndOctave(pitchClass: PitchClass, octave: Int): Int {
        return ((octave + 1) * MusicTheoryUtils.OCTAVE_SIZE) + pitchClass.value
    }

    private fun assertValidMidiNumber(midiNumber: Int) {
        if (midiNumber !in MusicTheoryUtils.MIN_MIDI_NUMBER..MusicTheoryUtils.MAX_MIDI_NUMBER) {
            throw IllegalArgumentException(
                    "midiNumber must be between ${MusicTheoryUtils.MIN_MIDI_NUMBER} and ${MusicTheoryUtils.MAX_MIDI_NUMBER}. " +
                            "midiNumber given: $midiNumber"
            )
        }
    }

}