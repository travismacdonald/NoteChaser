package com.cannonballapps.notechaser.models


import com.cannonballapps.notechaser.playablegenerator.Playable
import kotlinx.coroutines.Job
import timber.log.Timber
import javax.inject.Inject


class PlayablePlayer(val midiPlayer: MidiPlayer) {

    var qBpm = 90

    // TODO: keep reference to current coroutine (job i think)
    val curJob: Job? = null

    fun playPlayable(playable: Playable) {
        Timber.d("playing playable: ${playable.notes}")
    }

    fun stopCurPlayable() {
        
    }

    fun playerIsActive(): Boolean {
        return true
    }

//    private val curNotes: MutableSet<Note> = HashSet()

//    suspend fun playPlayable(playable: Playable) {
//        for (note in playable.notes) {
//            midiPlayer.playNote(note.ix)
//            curNotes.add(note)
//            delay(noteLengthMillis)
//            midiPlayer.stopNote(note.ix)
//            curNotes.remove(note)
//            if (spaceBetweenNotesMillis != 0L) {
//                delay(spaceBetweenNotesMillis)
//            }
//        }
//    }


}