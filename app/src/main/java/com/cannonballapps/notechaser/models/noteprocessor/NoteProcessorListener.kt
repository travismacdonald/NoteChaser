package com.cannonballapps.notechaser.models.noteprocessor

interface NoteProcessorListener {

    fun notifyNoteDetected(note: Int)

    fun notifyNoteUndetected(note: Int)
}
