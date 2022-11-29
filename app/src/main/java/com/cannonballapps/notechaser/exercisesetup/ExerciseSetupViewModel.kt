package com.cannonballapps.notechaser.exercisesetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonballapps.notechaser.common.ExerciseSettings
import com.cannonballapps.notechaser.common.ExerciseType
import com.cannonballapps.notechaser.common.SessionType
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.exercisesetup.ExerciseSetupUiState.Loading
import com.cannonballapps.notechaser.exercisesetup.ExerciseSetupUiState.Success
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.NotePoolType
import com.cannonballapps.notechaser.musicutilities.ParentScale2
import com.cannonballapps.notechaser.musicutilities.PitchClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseSetupViewModel @Inject constructor(
    private val prefsStore: PrefsStore,
) : ViewModel() {

    lateinit var exerciseType: ExerciseType

    val exerciseSettingsFlow: StateFlow<ExerciseSetupUiState> = prefsStore.exerciseSettingsFlow().map {
        Success(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = Loading,
    )

    val isValidConfiguration: StateFlow<Boolean> = exerciseSettingsFlow.map {
        hasNotePoolDegreesSelected() && hasSufficientRangeForPlayableGeneration()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = false,
    )

    fun prefetchPrefsStore() {
        viewModelScope.launch {
            prefsStore.exerciseSettingsFlow().first()
        }
    }

    fun saveChromaticDegrees(degrees: BooleanArray) {
        viewModelScope.launch {
            prefsStore.saveChromaticDegrees(degrees)
        }
    }

    fun saveDiatonicDegrees(degrees: BooleanArray) {
        viewModelScope.launch {
            prefsStore.saveDiatonicDegrees(degrees)
        }
    }

    fun saveMatchOctave(matchOctave: Boolean) {
        viewModelScope.launch {
            prefsStore.saveMatchOctave(matchOctave)
        }
    }

    fun saveModeIx(ix: Int) {
        viewModelScope.launch {
            prefsStore.saveModeIx(ix)
        }
    }

    fun saveNotePoolType(type: NotePoolType) {
        viewModelScope.launch {
            prefsStore.saveNotePoolType(type)
        }
    }

    fun saveNumQuestions(numQuestions: Int) {
        viewModelScope.launch {
            prefsStore.saveNumQuestions(numQuestions)
        }
    }

    fun saveParentScale(scale: ParentScale2) {
        viewModelScope.launch {
            prefsStore.saveParentScale(scale)
        }
    }

    fun savePlayStartingPitch(playPitch: Boolean) {
        viewModelScope.launch {
            prefsStore.savePlayStartingPitch(playPitch)
        }
    }

    fun savePlayableLowerBound(lower: Note) {
        viewModelScope.launch {
            prefsStore.savePlayableLowerBound(lower)
        }
    }

    fun savePlayableUpperBound(upper: Note) {
        viewModelScope.launch {
            prefsStore.savePlayableUpperBound(upper)
        }
    }

    fun saveQuestionKey(key: PitchClass) {
        viewModelScope.launch {
            prefsStore.saveQuestionKey(key)
        }
    }

    fun saveSessionTimeLimit(len: Int) {
        viewModelScope.launch {
            prefsStore.saveSessionTimeLimit(len)
        }
    }

    fun saveSessionType(sessionType: SessionType) {
        viewModelScope.launch {
            prefsStore.saveSessionType(sessionType)
        }
    }

    private fun hasNotePoolDegreesSelected(): Boolean {
        val uiState = exerciseSettingsFlow.value
        return if (uiState is Success) {
            when (uiState.exerciseSettings.notePoolType) {
                NotePoolType.DIATONIC -> {
                    uiState.exerciseSettings.diatonicDegrees.contains(true)
                }
                NotePoolType.CHROMATIC -> {
                    uiState.exerciseSettings.chromaticDegrees.contains(true)
                }
            }
        } else false
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
        return false
    }
}

sealed interface ExerciseSetupUiState {
    object Loading : ExerciseSetupUiState
    data class Success(val exerciseSettings: ExerciseSettings) : ExerciseSetupUiState
}
