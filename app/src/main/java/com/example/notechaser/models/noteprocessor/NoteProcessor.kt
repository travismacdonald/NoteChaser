package com.example.notechaser.models.noteprocessor

import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap

/**
 * Process incoming pitch data so that noise can be filtered out from actual notes.
 */
class NoteProcessor {

    private data class NoteStamp(val note: Int, val timeStamp: Long)

    // todo: tinker with this value
    private val PROBABILITY_THRESHOLD = 0.0f
    var noteCountThreshold = 0.7f

    var listener: NoteProcessorListener? = null

    // todo: either tinker, or turn this into a parameter
    var expirationLength = 500
    private val noteQueue: MutableList<NoteStamp> = LinkedList()
    private val noteCounts: MutableMap<Int, Int> = HashMap()
    private var lastPredictedNote = -1

//    private var mPrevNoteHeardIx = -1
//    private var mPrevHeardTimeStamp: Long = -1
//    private var mLastDetectedNoteIx = -1
//    private var mHasNotifiedDetected = false
//    private var mHasNotifiedUndetected = false


    // todo: for now, pitch detection is only monophonic;
    //       if polyphonic detection is added in later, then
    //       this will need to be reworked
    fun onPitchDetected(note: Int) {
        // todo: use other parameters later to better filter out noise / incorrect guesses

        // Remove expired notes
        val currentMillis = System.currentTimeMillis()
//        Timber.d("start: $currentMillis")
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

        noteQueuePush(note)

        // Find most occurring note
        var maxCount = -1
        var maxCountNote = -1
        for ((noteIx, count) in noteCounts) {
            if (count > maxCount) {
                maxCount = count
                maxCountNote = noteIx
            }
        }

        val predictionStrength = maxCount / (noteQueue.size).toFloat()
        Timber.d("note $maxCountNote prediction = $predictionStrength")
        if (maxCountNote != lastPredictedNote && predictionStrength > noteCountThreshold) {
            listener?.notifyNoteDetected(maxCountNote)
            lastPredictedNote = maxCountNote
        }

//        if (noteIx != mPrevNoteHeardIx) {
//            mHasNotifiedDetected = false
//            if (noteIx != -1) {
//                mPrevHeardTimeStamp = System.currentTimeMillis()
//            }
//            mPrevNoteHeardIx = noteIx
//        }
//        else if (noteIx != -1 && !mHasNotifiedDetected && hasMetFilterLengthThreshold()) {
//            if (listener != null) {
//                listener!!.notifyNoteDetected(noteIx)
//            }
//            mLastDetectedNoteIx = noteIx
//            mHasNotifiedUndetected = false
//            mHasNotifiedDetected = true
//        }
    }

//    private fun hasMetFilterLengthThreshold(): Boolean {
//        return (System.currentTimeMillis() - mPrevHeardTimeStamp).toInt() > millisRequired
//    }

    private fun noteQueuePop() {
        val noteStamp = noteQueue.removeAt(0)
        if (noteCounts[noteStamp.note] == 1) {
            noteCounts.remove(noteStamp.note)
        }
        else {
            noteCounts[noteStamp.note] = noteCounts[noteStamp.note]!! - 1
        }
        Timber.d("note popped: ${noteStamp.note}")
        Timber.d("queuesize = ${noteQueue.size}")
    }

    private fun noteQueuePush(note: Int) {
        val noteStamp = NoteStamp(note, System.currentTimeMillis())
        noteQueue.add(noteStamp)
        if (noteCounts.containsKey(note)) {
            noteCounts[note] = noteCounts[note]!! + 1
        }
        else {
            noteCounts[note] = 1
        }
        Timber.d("Note pushed: $note")
        Timber.d("queuesize = ${noteQueue.size}")
    }

}