package com.cannonballapps.notechaser.models

import jp.kshoji.javax.sound.midi.*
import kotlinx.coroutines.*

private const val NOTE_ON_VELOCITY = 70
private const val NOTE_OFF_VELOCITY = 0

class MidiPlayer2(private val synth: Synthesizer) {

    private var curMidiJob: Job? = null

    var isRunning = false
        private set

    private val receiver = synth.receiver

    fun close() {
        stop()
        synth.close()

    }

    fun stop() {
        synth.channels[0].allNotesOff()
        curMidiJob?.cancel()
    }

    suspend fun playChord(chord: List<Int>, chordLenInMillis: Long) {

        if (isRunning) {
            stop()
        }
        isRunning = true

        for (note in chord) {
            noteOn(note)
        }
        delay(chordLenInMillis)
        synth.channels[0].allNotesOff()
    }

    suspend fun playNoteSequence(seq: List<Int>, noteLenInMillis: Long) {
//        synth.channels[0].channelPressure = 50000
        // TODO: this works, just move it somewheres else
        synth.channels[0].controlChange(7, 160)
        if (isRunning) {
            stop()
        }
        isRunning = true

        for (note in seq) {
            noteOn(note)
            delay(noteLenInMillis)
            noteOff(note)
        }

    }

    private fun noteOn(midiNumber: Int) {
        val msg = ShortMessage(ShortMessage.NOTE_ON, midiNumber, NOTE_ON_VELOCITY)
        receiver.send(msg, -1)
    }

    private fun noteOff(midiNumber: Int) {
        val msg = ShortMessage(ShortMessage.NOTE_OFF, midiNumber, NOTE_OFF_VELOCITY)
        receiver.send(msg, -1)
    }

}