package com.cannonballapps.notechaser.exercisesetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonballapps.notechaser.common.ExerciseSettingsAssembler
import com.cannonballapps.notechaser.common.ExerciseType
import com.cannonballapps.notechaser.common.NotePoolType
import com.cannonballapps.notechaser.common.SessionLengthSettings
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.Scale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseSetupViewModel @Inject constructor(
    private val prefsStore: PrefsStore,
) : ViewModel() {

    // todo refactor
    lateinit var exerciseType: ExerciseType

    // todo inject assembler
    private val exerciseSettingsAssembler = ExerciseSettingsAssembler()
    val exerciseSettingsFlow = exerciseSettingsAssembler.exerciseSettingsFlow

    val isValidConfiguration: StateFlow<Boolean> = exerciseSettingsFlow.map {
        hasNotePoolDegreesSelected() && hasSufficientRangeForPlayableGeneration()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = false,
    )

    val notePoolTypes: List<NotePoolType>
        get() = listOf(
            exerciseSettingsAssembler.chromaticPoolType,
            exerciseSettingsAssembler.diatonicPoolType,
        )

    val sessionLengthTypes: List<SessionLengthSettings>
        get() = listOf(
            exerciseSettingsAssembler.sessionLengthQuestionLimit,
            exerciseSettingsAssembler.sessionLengthTimeLimit,
            exerciseSettingsAssembler.sessionLengthNoLimit,
        )

    fun saveExerciseSettings() {
        // todo look into an applicationScope implementation
        viewModelScope.launch {
            // todo save object in room, once refactored
            prefsStore.saveExerciseSettings(exerciseSettingsFlow.value)
        }
    }

    fun setChromaticDegrees(degrees: BooleanArray) {
        exerciseSettingsAssembler.setChromaticDegrees(degrees)
    }

    fun setDiatonicDegrees(degrees: BooleanArray) {
        exerciseSettingsAssembler.setDiatonicDegrees(degrees)
    }

    fun setMatchOctave(shouldMatchOctave: Boolean) {
        exerciseSettingsAssembler.setShouldMatchOctave(shouldMatchOctave)
    }

    fun setScale(scale: Scale) {
        exerciseSettingsAssembler.setScale(scale)
    }

    fun setNotePoolType(notePoolType: NotePoolType) {
        exerciseSettingsAssembler.notePoolType = notePoolType
    }

    fun setNumQuestions(numQuestions: Int) {
        exerciseSettingsAssembler.setNumQuestions(numQuestions)
    }

    fun setShouldPlayStartingPitch(shouldPlayStartingPitch: Boolean) {
        exerciseSettingsAssembler.setShouldPlayStartingPitch(shouldPlayStartingPitch)
    }

    fun setPlayableLowerBound(lowerBound: Note) {
        exerciseSettingsAssembler.setPlayableLowerBound(lowerBound)
    }

    fun setPlayableUpperBound(upperBound: Note) {
        exerciseSettingsAssembler.setPlayableUpperBound(upperBound)
    }

    fun setQuestionKey(key: PitchClass) {
        exerciseSettingsAssembler.setQuestionKey(key)
    }

    fun setTimeLimitMinutes(timeLimitMinutes: Int) {
        exerciseSettingsAssembler.setTimeLimitMinutes(timeLimitMinutes)
    }

    fun setSessionLengthType(sessionLengthSettings: SessionLengthSettings) {
        exerciseSettingsAssembler.sessionLengthSettings = sessionLengthSettings
    }

    private fun hasNotePoolDegreesSelected(): Boolean {
        return when (val notePoolType = exerciseSettingsAssembler.notePoolType) {
            is NotePoolType.Diatonic -> {
                notePoolType.degrees.contains(true)
            }
            is NotePoolType.Chromatic -> {
                notePoolType.degrees.contains(true)
            }
        }
    }

    private fun hasSufficientRangeForPlayableGeneration(): Boolean {
        // TODO
//        lateinit var intervals: IntArray
//        if (notePoolType.value == NotePoolType.DIATONIC) {
//            intervals = MusicTheoryUtils.transformDiatonicDegreesToIntervals(
//                diatonicDegrees.value!!,
//                scale.value!!.intervals,
//                questionKey.value!!.value
//            )
//        } else if (notePoolType.value == NotePoolType.CHROMATIC) {
//            intervals = MusicTheoryUtils.transformChromaticDegreesToIntervals(
//                chromaticDegrees.value!!,
//                questionKey.value!!.value
//            )
//        }
//        for (interval in intervals) {
//            val lower = playableLowerBound.value!!
//            val upper = playableUpperBound.value!!
//            val pitchClass = MusicTheoryUtils.CHROMATIC_PITCH_CLASSES_FLAT[interval]
//            if (!MusicTheoryUtils.pitchClassOccursBetweenNoteBounds(pitchClass, lower, upper)) {
//                return false
//            }
//        }
        return true
    }
}
