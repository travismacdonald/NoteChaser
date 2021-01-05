package com.cannonballapps.notechaser.models


import com.cannonballapps.notechaser.playablegenerator.Playable
import kotlinx.coroutines.*

const val NUM_MILLIS_IN_MINUTE = 60000L
const val REVERB_LEN_MILLIS = 600L

class PlayablePlayer(val midiPlayer: MidiPlayer2) {

    var quarterNoteBpm = 30
    private val noteLenInMillis: Long
        get() = NUM_MILLIS_IN_MINUTE / quarterNoteBpm

    fun playPlayable(playable: Playable): Job {
        // TODO: fine for now, but have to refactor later when introducing more complex playable
        val midiNumbers = playable.notes.map { it.midiNumber }
        return GlobalScope.launch {
            midiPlayer.playNoteSequence(midiNumbers, noteLenInMillis)
            // Account for reverb that persists after final note has finished playing
            delay(REVERB_LEN_MILLIS)
        }

    }

    fun stopCurPlayable(): Job {
        return GlobalScope.launch {
            midiPlayer.stop()
            // Account for reverb that persists after final note has finished playing
            delay(REVERB_LEN_MILLIS)
        }
    }

    fun isPlaying(): Boolean {
        return false
    }

}