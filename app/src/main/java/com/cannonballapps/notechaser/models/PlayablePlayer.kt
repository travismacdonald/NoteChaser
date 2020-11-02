package com.cannonballapps.notechaser.models

import com.cannonballapps.notechaser.playablegenerator.Note
import com.cannonballapps.notechaser.playablegenerator.Playable
import kotlinx.coroutines.delay

class PlayablePlayer(val midiPlayer: MidiPlayer) {

    var noteLengthMillis: Long = 1000

    var spaceBetweenNotesMillis: Long = 0

    private val curNotes: MutableSet<Note> = HashSet()

    suspend fun playPlayable(playable: Playable) {
        for (note in playable.notes) {
            midiPlayer.playNote(note.ix)
            curNotes.add(note)
            delay(noteLengthMillis)
            midiPlayer.stopNote(note.ix)
            curNotes.remove(note)
            if (spaceBetweenNotesMillis != 0L) {
                delay(spaceBetweenNotesMillis)
            }
        }
    }



}