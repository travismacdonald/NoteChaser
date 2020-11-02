package com.example.notechaser.models.noteprocessor

import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap

/**
 * Process incoming pitch data so that noise can be filtered out from actual notes.
 */
class NoteProcessor {

    private data class NoteStamp(val note: Int, val timeStamp: Long)

    // Flag that indicating that the note queue is full
    // (i.e. ready for pitch analysis)
    var initTimeHasPassed: Boolean = false


    var initTimeMillis: Long? = null

    var notePredictionThreshold = 0.55f

    var listener: NoteProcessorListener? = null

    // todo: either tinker, or turn this into a parameter
    var expirationLength = 750

    private val noteQueue: MutableList<NoteStamp> = LinkedList()

    // Store the frequency of each unique note value
    private val noteCounts: MutableMap<Int, Int> = HashMap()

    // `null` value represents not yet used; `-1` denotes silence
    private var lastPredictedNote: Int? = null

    // todo: for now, pitch detection is only monophonic;
    //       if polyphonic detection is added in later, then
    //       this will need to be reworked
    /**
     *
     */
    fun onPitchDetected(note: Int) {
        val currentMillis = System.currentTimeMillis()
        if (initTimeMillis == null) {
            initTimeMillis = currentMillis
        }
        noteQueuePush(note)
        removeExpiredNotes()

        // Only analyze the note queue once it has had some time to fill up
        // (otherwise it will analyze 1...n samples)
        if (initTimeHasPassed) {
            // Find most frequent note
            var maxCount = -1
            var maxCountNote = -1
            for ((noteIx, count) in noteCounts) {
                if (count > maxCount) {
                    maxCount = count
                    maxCountNote = noteIx
                }
            }

            val predictionStrength = maxCount / (noteQueue.size).toFloat()
            // Note change detected, meets probability threshold
            if (maxCountNote != lastPredictedNote && predictionStrength >= notePredictionThreshold) {
                lastPredictedNote?.let {
                    listener?.notifyNoteUndetected(it)
                }
                Timber.d("NOTEPRED : note $maxCountNote prediction = $predictionStrength")
                listener?.notifyNoteDetected(maxCountNote)
                lastPredictedNote = maxCountNote
            }
            // Junk detected
            else if (predictionStrength < notePredictionThreshold) {
                lastPredictedNote?.let {
                    listener?.notifyNoteUndetected(it)
                }
                // TODO: move this up in the brackets?
                lastPredictedNote = null

            }
        }
        else {
            initTimeMillis?.let {
                initTimeHasPassed = (currentMillis - it) > expirationLength
            }

        }
        val outStr = noteQueue.map { it.note }
        Timber.d(" NOTEQ : $outStr")
    }

    /**
     * Resets variables to their default values.
     */
    fun clear() {
        noteQueue.clear()
        noteCounts.clear()
        lastPredictedNote = null
        initTimeMillis = null
        initTimeHasPassed = false
    }

    /**
     * Pops note from queue and updates the count of the popped note.
     */
    private fun noteQueuePop() {
        val noteStamp = noteQueue.removeAt(0)
        if (noteCounts[noteStamp.note] == 1) {
            noteCounts.remove(noteStamp.note)
        }
        else {
            noteCounts[noteStamp.note] = noteCounts[noteStamp.note]!! - 1
        }
    }

    /**
     * Pushes note to queue and updates the count of pushed note.
     */
    private fun noteQueuePush(note: Int) {
        val noteStamp = NoteStamp(note, System.currentTimeMillis())
        noteQueue.add(noteStamp)
        if (noteCounts.containsKey(note)) {
            noteCounts[note] = noteCounts[note]!! + 1
        }
        else {
            noteCounts[note] = 1
        }
    }

    /**
     * Removes all notes in the queue that have exceeded the note expiration threshold.
     */
    private fun removeExpiredNotes() {
        val currentMillis = System.currentTimeMillis()
        var numRemove = 0
        for (noteStamp in noteQueue) {
            if (currentMillis - noteStamp.timeStamp > expirationLength) {
                numRemove++
            }
            else break
        }
        for (i in 0 until numRemove) {
            noteQueuePop()
        }
    }

}