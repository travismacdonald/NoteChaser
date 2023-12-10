package com.cannonballapps.notechaser.common.noteprocessor

import com.cannonballapps.notechaser.musicutilities.Note
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class NoteDetector {
    val noteDetectionFlow: SharedFlow<NoteDetectionResult> = MutableSharedFlow()
}

sealed interface NoteDetectionResult {
    data class Value(
        val note: Note,
        val probability: Float,
    ) : NoteDetectionResult

    object None : NoteDetectionResult
}

@OptIn(ExperimentalContracts::class)
fun NoteDetectionResult.isNoneResult(): Boolean {
    contract {
        returns(true) implies (this@isNoneResult is NoteDetectionResult.None)
    }

    return this is NoteDetectionResult.None
}
