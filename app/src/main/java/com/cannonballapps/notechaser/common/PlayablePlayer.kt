package com.cannonballapps.notechaser.common

import com.cannonballapps.notechaser.musicutilities.playablegenerator.Playable
import kotlinx.coroutines.delay

const val NUM_MILLIS_IN_MINUTE = 60000L

// TODO: this is set at a pretty conservative value right now
const val REVERB_LEN_MILLIS = 600L

class PlayablePlayer(val midiPlayer: MidiPlayer2) {

    // TODO: This should be a user parameter.
    var quarterNoteBpm = 90
    private val noteLenInMillis: Long
        get() = NUM_MILLIS_IN_MINUTE / quarterNoteBpm

    suspend fun playPlayable(playable: Playable) {
        // TODO: fine for now, but have to refactor later when introducing more complex playable
        val midiNumbers = playable.notes.map { it.midiNumber }

        midiPlayer.playNoteSequence(midiNumbers, noteLenInMillis)
        delay(REVERB_LEN_MILLIS)
    }

    fun playPlayable2(
        playable: Playable,
        onPlaybackFinished: (() -> Unit)? = null,
    ) {
        // TODO
    }

    fun stopCurPlayable() {
        midiPlayer.stop()
    }
}
