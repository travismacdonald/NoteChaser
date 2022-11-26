package com.cannonballapps.notechaser.common.noteprocessor

interface NoteProcessorListener {

    fun notifyNoteDetected(note: Int)

    fun notifyNoteUndetected(note: Int)
}
