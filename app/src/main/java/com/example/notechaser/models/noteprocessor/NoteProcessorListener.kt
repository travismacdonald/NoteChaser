package com.example.notechaser.models.noteprocessor

interface NoteProcessorListener {

    fun notifyNoteDetected(note: Int)

    fun notifyNoteUndetected(note: Int)

    @Deprecated("fuck off, compiler")
    fun notifySilenceDetected()

}