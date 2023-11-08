package com.cannonballapps.notechaser.exercisesetup

import android.util.Range
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonballapps.notechaser.common.ExerciseSettings
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
    private val exerciseSettingsAssembler: ExerciseSettingsAssembler,
) : ViewModel() {

    // todo refactor
    lateinit var exerciseType: ExerciseType

    val exerciseSettingsStream: StateFlow<ExerciseSettings> = exerciseSettingsAssembler.exerciseSettingsFlow

    val isValidConfiguration: StateFlow<Boolean> = exerciseSettingsStream.map {
        hasNotePoolDegreesSelected() && hasSufficientRangeForPlayableGeneration()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
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
            prefsStore.saveExerciseSettings(exerciseSettingsStream.value)
        }
    }

    fun setChromaticDegrees(degrees: BooleanArray) {
        exerciseSettingsAssembler.setChromaticDegrees(degrees)
    }

    fun setDiatonicDegrees(degrees: BooleanArray) {
        exerciseSettingsAssembler.setDiatonicDegrees(degrees)
    }

    fun onMatchOctaveCheckChanged(shouldMatchOctave: Boolean) {
        exerciseSettingsAssembler.setShouldMatchOctave(shouldMatchOctave)
    }

    fun setScale(scale: Scale) {
        exerciseSettingsAssembler.setScale(scale)
    }

    fun setNotePoolType(notePoolType: NotePoolType) {
        exerciseSettingsAssembler.notePoolType = notePoolType
    }

    fun onNumQuestionsChange(numQuestions: Int) {
        exerciseSettingsAssembler.setNumQuestions(numQuestions)
    }

    fun onPlayStartingPitchCheckChanged(shouldPlayStartingPitch: Boolean) {
        exerciseSettingsAssembler.setShouldPlayStartingPitch(shouldPlayStartingPitch)
    }

    fun onPlayableBoundsChanged(range: Range<Note>) {
        exerciseSettingsAssembler.setPlayableBounds(range)
    }

    fun setQuestionKey(key: PitchClass) {
        exerciseSettingsAssembler.setQuestionKey(key)
    }

    fun onTimeLimitMinutesChange(timeLimitMinutes: Int) {
        exerciseSettingsAssembler.setTimeLimitMinutes(timeLimitMinutes)
    }

    fun setSessionLengthTypeQuestionLimit() {
        exerciseSettingsAssembler.setSessionLengthTypeQuestionLimit()
    }

    fun setSessionLengthTypeTimeLimit() {
        exerciseSettingsAssembler.setSessionLengthTypeTimeLimit()
    }

    fun setSessionLengthTypeNoLimit() {
        exerciseSettingsAssembler.setSessionLengthTypeNoLimit()
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
