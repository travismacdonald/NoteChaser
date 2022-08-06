package com.cannonballapps.notechaser.playablegenerator

import com.cannonballapps.notechaser.musicutilities.Note

object PlayableFactory {

    fun makePlayableFromNote(note: Note): Playable {
        return Playable(arrayListOf(note), PlaybackType.HARMONIC)
    }

    fun makePlayableFromMidiNumber(midiNumber: Int): Playable {
        val note = Note(midiNumber)
        return Playable(arrayListOf(note), PlaybackType.HARMONIC)
    }
}
