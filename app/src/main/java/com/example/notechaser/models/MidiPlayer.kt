package com.example.notechaser.models

import cn.sherlock.com.sun.media.sound.SF2Soundbank
import cn.sherlock.com.sun.media.sound.SoftSynthesizer
import jp.kshoji.javax.sound.midi.InvalidMidiDataException
import jp.kshoji.javax.sound.midi.Receiver
import jp.kshoji.javax.sound.midi.ShortMessage
import timber.log.Timber
import java.util.*

// todo: there is likely a bug with muting/unmuting; look into it later if it pops up
class MidiPlayer {

    private val START = 0X90

    private val STOP = 0X80

    private val PROGRAM_CHANGE = 0XC0

    private val VOLUME_OFF = 0

    private val DEFAULT_VOLUME = 65

    private var synth: SoftSynthesizer = SoftSynthesizer()

    private var recv: Receiver? = null

    private val activeNotes: MutableSet<Int> = HashSet()

    private var plugin: Int = -1

    private var volume = DEFAULT_VOLUME

    var isRunning: Boolean = false
    private set

    var sf2: SF2Soundbank? = null
        set(value) {
            field = value

            sendMidiSetup()

        }

    var isMuted: Boolean = false
        private set

    fun start() {
        sendMidiSetup()
        isRunning = true
    }

    fun stop() {
        clear()
        synth.close()
        isRunning = false
    }

    fun playNote(note: Int) {
        if (!noteIsActive(note)) {
            noteOn(note)
            activeNotes.add(note)
        }
    }

    fun stopNote(note: Int) {
        if (noteIsActive(note)) {
            noteOff(note)
            activeNotes.remove(note)
        }
    }

    fun playChord(chord: List<Int>) {
        for (note in chord) {
            playNote(note)
        }
    }

    fun stopChord(chord: List<Int>) {
        for (note in chord) {
            stopNote(note)
        }
    }

    fun clear() {
        for (note in activeNotes) {
            noteOff(note)
        }
        activeNotes.clear()
    }

    fun noteIsActive(note: Int): Boolean {
        return activeNotes.contains(note)
    }

    fun hasActiveNotes(): Boolean {
        return !activeNotes.isEmpty()
    }

    fun getPlugin(): Int {
        return plugin
    }

    // todo: clean this up
    fun setPlugin(plugin: Int) {
        if (this.plugin != plugin) {
            this.plugin = plugin
//            if (mDriver != null) {
//                // Need to write plugin to midi driver.
//                sendMidiSetup()
//                if (hasActiveNotes()) {
//                    refreshPlayback()
//                }
//            }
        }
    }

    fun getVolume(): Int {
        return volume
    }

    fun setVolume(volume: Int) {
        if (volume != this.volume) {
            this.volume = volume
            if (hasActiveNotes()) {
                refreshPlayback()
            }
        }
    }

    fun mute() {
        setVolume(VOLUME_OFF)
        isMuted = true
    }

    fun unMute() {
        setVolume(volume)
        isMuted = false
    }

    /*
     * noteOn and noteOff exist because they don't have the side effect of adding and removing notes
     * to mActiveNotes.
     * Methods refreshPlayback and clear require this to avoid throwing ConcurrentModificationException()
     */
    private fun noteOn(note: Int) {
        try {
            val msg = ShortMessage()
            msg.setMessage(ShortMessage.NOTE_ON, 0, note, volume)
            recv?.send(msg, -1)
        } catch (e: InvalidMidiDataException) {
            e.printStackTrace()
        }
    }

    private fun noteOff(note: Int) {
        try {
            val msg = ShortMessage()
            msg.setMessage(ShortMessage.NOTE_OFF, 0, note, volume)
            recv?.send(msg, -1)
        } catch (e: InvalidMidiDataException) {
            e.printStackTrace()
        }
    }

    private fun sendMidiSetup() {
        if (sf2 != null) {
            Timber.i("1")
            synth.open()
            Timber.i("2")
            synth.loadAllInstruments(sf2!!)
            Timber.i("3")
            synth.channels[0].programChange(0)
            Timber.i("4")
            recv = synth.receiver
            Timber.i("5")
        }
    }

    private fun refreshPlayback() {
        for (note in activeNotes) {
            noteOff(note)
        }
        for (note in activeNotes) {
            noteOn(note)
        }
    }

}