package com.cannonballapps.notechaser.common.noteprocessor

import com.cannonballapps.notechaser.musicutilities.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NoteDetector {
    val noteDetectionFlow: Flow<NoteDetectionResult> = flow { /* TODO */ }
}

sealed interface NoteDetectionResult {
    data class Value(
        val note: Note,
        val probability: Float,
    ) : NoteDetectionResult

    object None : NoteDetectionResult
}
