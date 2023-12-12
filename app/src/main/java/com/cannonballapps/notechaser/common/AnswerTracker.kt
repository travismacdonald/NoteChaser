package com.cannonballapps.notechaser.common

import com.cannonballapps.notechaser.musicutilities.Note

class AnswerTracker {

    val trackedAnswer: List<Note> = emptyList()

    val lastTrackedNote: Note?
        get() = trackedAnswer.lastOrNull()

    /**
     * @return true if the tracked answer matches the expected answer.
     */
    fun trackNote(note: Note): Boolean = false
}
