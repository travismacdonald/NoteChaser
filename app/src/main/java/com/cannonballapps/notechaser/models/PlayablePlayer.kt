package com.cannonballapps.notechaser.models


import com.cannonballapps.notechaser.playablegenerator.Playable
import kotlinx.coroutines.*

const val NUM_MILLIS_IN_MINUTE = 60000L
const val REVERB_LEN_MILLIS = 1000L

class PlayablePlayer(val midiPlayer: MidiPlayer2) {

    var quarterNoteBpm = 120
    private val noteLenInMillis: Long
        get() = NUM_MILLIS_IN_MINUTE / quarterNoteBpm

    suspend fun playPlayable(playable: Playable) {
        // TODO: fine for now, but have to refactor later when introducing more complex playable
        val midiNumbers = playable.notes.map { it.midiNumber }
        midiPlayer.playNoteSequence(midiNumbers, noteLenInMillis)
        delay(REVERB_LEN_MILLIS)
    }

    suspend fun stopCurPlayable() {
        midiPlayer.stop()
        delay(REVERB_LEN_MILLIS)
    }

}