package com.cannonballapps.shared.models.noteprocessor

interface NoteProcessorListener {

    fun notifyNoteDetected(note: Int)

    fun notifyNoteUndetected(note: Int)
}
