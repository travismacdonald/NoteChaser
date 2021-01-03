package com.cannonballapps.notechaser.playablegenerator

import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.NoteFactory

object PlayableFactory {

    fun makePlayableFromNote(note: Note): Playable {
        return Playable(arrayListOf(note), PlaybackType.HARMONIC)
    }

    fun makePlayableFromMidiNumber(midiNumber: Int): Playable {
        val note = NoteFactory.makeNoteFromMidiNumber(midiNumber)
        return Playable(arrayListOf(note), PlaybackType.HARMONIC)
    }

}