package com.cannonballapps.notechaser.common

import android.util.Range
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.Scale

data class ExerciseSettings(
    val notePoolType: NotePoolType,
    val sessionQuestionSettings: SessionQuestionSettings,
    val sessionLengthSettings: SessionLengthSettings,
)

sealed interface SessionLengthSettings {
    data class TimeLimit(
        val timeLimitMinutes: Int,
    ) : SessionLengthSettings

    data class QuestionLimit(
        val numQuestions: Int,
    ) : SessionLengthSettings

    object NoLimit : SessionLengthSettings
}

sealed interface NotePoolType {
    data class Chromatic(
        val degrees: BooleanArray, // todo inspection, enforce size
    ) : NotePoolType

    data class Diatonic(
        val degrees: BooleanArray, // todo inspection, enforce size
        val scale: Scale,
    ) : NotePoolType
}

data class SessionQuestionSettings(
    val questionKey: PitchClass,
    val questionKeyValues: List<PitchClass>,
    val shouldMatchOctave: Boolean,
    val shouldPlayStartingPitch: Boolean,
    val playableBounds: Range<Note>,
)
