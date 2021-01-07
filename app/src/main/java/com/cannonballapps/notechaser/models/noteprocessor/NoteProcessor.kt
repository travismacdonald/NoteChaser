package com.cannonballapps.notechaser.models.noteprocessor

import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import java.util.*
import kotlin.collections.HashMap


class NoteProcessor {

    var listener: NoteProcessorListener? = null

    // Flag that indicating that the note queue is full
    // (i.e. ready for pitch analysis)
    private var readyToFilter: Boolean = false
    private var initTimeMillis: Long? = null

    private val noteExpirationLenInMillis = 600L
    private val notePredictionThreshold = 0.45f

    private val noteQueue: MutableList<NoteStamp> = LinkedList()
    private val noteFrequencies: MutableMap<Int, Int> = HashMap()

    private var lastPredictedNote: Int? = null

    init {
        resetNoteFrequenciesMap()
    }

    // TODO: Pitch detection is currently monophonic. If future implementations use polyphonic
    //       detection, this will have to be reworked.
    fun onPitchDetected(note: Int?) {
        if (initTimeMillis == null) {
            initTimeMillis = System.currentTimeMillis()
        }
        removeExpiredNotes()
        addNoteToQueue(note)

        if (readyToFilter) {
            makeNotePrediction()
        }
        else {
            checkIfReadyToFilter()
        }
    }

    fun clear() {
        noteQueue.clear()
        resetNoteFrequenciesMap()

        lastPredictedNote = null
        initTimeMillis = null
        readyToFilter = false
    }

    private fun checkIfReadyToFilter() {
        val timeSinceInit = System.currentTimeMillis() - initTimeMillis!!
        readyToFilter = timeSinceInit > noteExpirationLenInMillis
    }

    private fun makeNotePrediction() {
        val filtered = noteFrequencies.filter { it.value > 0 }
        val mostFrequentNote = filtered.maxByOrNull { it.value }?.key

        if (mostFrequentNote == null || !noteMeetsFilterThreshold(mostFrequentNote)) {
            lastPredictedNote?.let {
                listener?.notifyNoteUndetected(it)
                lastPredictedNote = null
            }
        }
        else if (mostFrequentNote != lastPredictedNote) {
            listener?.notifyNoteDetected(mostFrequentNote)
            lastPredictedNote = mostFrequentNote
        }
    }

    private fun noteMeetsFilterThreshold(note: Int): Boolean {
        val predictionScore = noteFrequencies[note]!! / (noteQueue.size).toFloat()

        return predictionScore >= notePredictionThreshold
    }

    private fun removeExpiredNotes() {
        // TODO: can probably use kotlin function `filter { ... }`
        val currentMillis = System.currentTimeMillis()
        var numRemove = 0
        for (noteStamp in noteQueue) {
            if (currentMillis - noteStamp.timeStamp > noteExpirationLenInMillis) {
                numRemove++
            }
            else break
        }
        for (i in 0 until numRemove) {
            popNoteQueue()
        }
    }

    private fun addNoteToQueue(note: Int?) {
        val noteStamp = NoteStamp(note, System.currentTimeMillis())
        noteQueue.add(noteStamp)
        note?.let {
            noteFrequencies[note] = noteFrequencies[note]!! + 1
        }
    }

    private fun popNoteQueue() {
        val toRemove = noteQueue.removeAt(0).note
        toRemove?.let {
            noteFrequencies[toRemove] = noteFrequencies[toRemove]!! - 1
        }
    }

    private fun resetNoteFrequenciesMap() {
        for (midiNum in MusicTheoryUtils.MIN_MIDI_NUMBER..MusicTheoryUtils.MAX_MIDI_NUMBER) {
            noteFrequencies[midiNum] = 0
        }
    }

    private data class NoteStamp(val note: Int?, val timeStamp: Long)

}