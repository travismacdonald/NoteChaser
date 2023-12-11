package com.cannonballapps.notechaser.common

import com.cannonballapps.notechaser.musicutilities.Note

class AnswerTracker {

    val trackedAnswer: List<Note> = emptyList()

    val lastTrackedNote: Note?
        get() = trackedAnswer.lastOrNull()

    fun trackNote(note: Note) { /* TODO */ }
}
