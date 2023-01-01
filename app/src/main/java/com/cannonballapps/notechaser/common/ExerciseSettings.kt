package com.cannonballapps.notechaser.common

import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.Scale

data class ExerciseSettings(
    val notePoolType: NotePoolType,
    val sessionSettings: SessionSettings,
    val sessionLengthSettings: SessionLengthSettings,
)

sealed interface SessionLengthSettings {
    data class TimeLimit(
        val timeLimitMinutes: Int,
    ) : SessionLengthSettings

    data class QuestionLimit(
        val questionLimit: Int,
    ) : SessionLengthSettings

    object NoLimit : SessionLengthSettings
}

sealed interface NotePoolType {
    data class Chromatic(
        val degrees: BooleanArray,
    ) : NotePoolType

    data class Diatonic(
        val degrees: BooleanArray,
        val scale: Scale,
    ) : NotePoolType
}

// todo better name?
data class SessionSettings(
    val questionKey: PitchClass,
    val shouldMatchOctave: Boolean,
    val shouldPlayStartingPitch: Boolean,
    val playableLowerBound: Note,
    val playableUpperBound: Note,
)
