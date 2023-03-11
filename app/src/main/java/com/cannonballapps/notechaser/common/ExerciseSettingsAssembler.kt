package com.cannonballapps.notechaser.common

import com.cannonballapps.notechaser.musicutilities.Ionian
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.ParentScale
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.Scale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


// todo unit tests
class ExerciseSettingsAssembler @Inject constructor() {


    /**
     * NotePoolType settings
     */

    var chromaticPoolType = NotePoolType.Chromatic(
        degrees = booleanArrayOf(
            true, true, true, true, true, true,
            true, true, true, true, true, true,
        ),
    )

    var diatonicPoolType = NotePoolType.Diatonic(
        degrees = booleanArrayOf(
            true, true, true, true, true, true, true,
        ),
        scale = ParentScale.Major.Ionian,
    )

    var notePoolType: NotePoolType = chromaticPoolType

    /**
     * SessionSettings
     */

    private var sessionSettings = SessionSettings(
        questionKey = PitchClass.C,
        shouldMatchOctave = false,
        shouldPlayStartingPitch = true,
        playableLowerBound = Note(midiNumber = 48),
        playableUpperBound = Note(midiNumber = 72),
    )

    /**
     * SessionLengthSettings
     */

    var sessionLengthQuestionLimit = SessionLengthSettings.QuestionLimit(
        numQuestions = 20,
    )

    var sessionLengthTimeLimit = SessionLengthSettings.TimeLimit(
        timeLimitMinutes = 5,
    )

    var sessionLengthNoLimit = SessionLengthSettings.NoLimit

    var sessionLengthSettings: SessionLengthSettings = sessionLengthQuestionLimit

    /**
     * ExerciseSettings flows
     */

    private val _exerciseSettingsFlow: MutableStateFlow<ExerciseSettings> = MutableStateFlow(
        assembleExerciseSettings(),
    )
    val exerciseSettingsFlow: StateFlow<ExerciseSettings> = _exerciseSettingsFlow.asStateFlow()

    /**
     * Assemble functions
     */

    fun setChromaticDegrees(chromaticDegrees: BooleanArray) {
        chromaticPoolType = chromaticPoolType.copy(
            degrees = chromaticDegrees,
        )
        notePoolType = chromaticPoolType
        updateExerciseSettings()
    }

    fun setDiatonicDegrees(diatonicDegrees: BooleanArray) {
        diatonicPoolType = diatonicPoolType.copy(
            degrees = diatonicDegrees,
        )
        notePoolType = diatonicPoolType
        updateExerciseSettings()
    }

    fun setShouldMatchOctave(shouldMatchOctave: Boolean) {
        sessionSettings = sessionSettings.copy(
            shouldMatchOctave = shouldMatchOctave,
        )
        updateExerciseSettings()
    }

    fun setScale(scale: Scale) {
        diatonicPoolType = diatonicPoolType.copy(
            scale = scale,
        )
        notePoolType = diatonicPoolType
        updateExerciseSettings()
    }

    fun setSessionLengthTypeQuestionLimit() {
        sessionLengthSettings = sessionLengthQuestionLimit
        updateExerciseSettings()
    }

    fun setSessionLengthTypeTimeLimit() {
        sessionLengthSettings = sessionLengthTimeLimit
        updateExerciseSettings()
    }

    fun setSessionLengthTypeNoLimit() {
        sessionLengthSettings = sessionLengthNoLimit
        updateExerciseSettings()
    }


    fun setNumQuestions(numQuestions: Int) {
        sessionLengthQuestionLimit = sessionLengthQuestionLimit.copy(
            numQuestions = numQuestions,
        )
        sessionLengthSettings = sessionLengthQuestionLimit
        updateExerciseSettings()
    }

    fun setTimeLimitMinutes(timeLimitMinutes: Int) {
        sessionLengthTimeLimit = sessionLengthTimeLimit.copy(
            timeLimitMinutes = timeLimitMinutes,
        )
        sessionLengthSettings = sessionLengthTimeLimit
        updateExerciseSettings()
    }

    fun setShouldPlayStartingPitch(shouldPlayStartingPitch: Boolean) {
        sessionSettings = sessionSettings.copy(
            shouldPlayStartingPitch = shouldPlayStartingPitch,
        )
        updateExerciseSettings()
    }

    fun setPlayableLowerBound(lowerBound: Note) {
        sessionSettings = sessionSettings.copy(
            playableLowerBound = lowerBound,
        )
        updateExerciseSettings()
    }

    fun setPlayableUpperBound(upperBound: Note) {
        sessionSettings = sessionSettings.copy(
            playableUpperBound = upperBound,
        )
        updateExerciseSettings()
    }

    fun setQuestionKey(key: PitchClass) {
        sessionSettings = sessionSettings.copy(
            questionKey = key,
        )
        updateExerciseSettings()
    }

    private fun updateExerciseSettings() {
        _exerciseSettingsFlow.value = assembleExerciseSettings()
    }

    private fun assembleExerciseSettings(): ExerciseSettings = ExerciseSettings(
        notePoolType = notePoolType,
        sessionSettings = sessionSettings,
        sessionLengthSettings = sessionLengthSettings,
    )
}
