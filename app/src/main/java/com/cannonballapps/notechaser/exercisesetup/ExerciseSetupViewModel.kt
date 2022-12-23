package com.cannonballapps.notechaser.exercisesetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonballapps.notechaser.common.ExerciseSettings
import com.cannonballapps.notechaser.common.ExerciseType
import com.cannonballapps.notechaser.common.SessionType
import com.cannonballapps.notechaser.common.prefsstore.PrefsStore
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.NotePoolType
import com.cannonballapps.notechaser.musicutilities.ParentScale2
import com.cannonballapps.notechaser.musicutilities.PitchClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseSetupViewModel @Inject constructor(
    private val prefsStore: PrefsStore,
) : ViewModel() {

    lateinit var exerciseType: ExerciseType

    private val _exerciseSettingsFlow2: MutableStateFlow<ExerciseSettings> = MutableStateFlow(
        // TODO move default values to a better location
        ExerciseSettings(
            chromaticDegrees = booleanArrayOf(
                true, true, true, true, true, true,
                true, true, true, true, true, true,
            ),
            diatonicDegrees = booleanArrayOf(
                true,
                false,
                true,
                false,
                true,
                false,
                false,
            ),
            modeIx = 0,
            notePoolType = NotePoolType.DIATONIC,
            numQuestions = 20,
            parentScale = ParentScale2.MAJOR,
            matchOctave = false,
            playStartingPitch = true,
            playableLowerBound = Note(48),
            playableUpperBound = Note(72),
            questionKey = PitchClass.C,
            sessionTimeLimit = 10,
            sessionType = SessionType.QUESTION_LIMIT,
        ),
    )
    val exerciseSettingsFlow2: StateFlow<ExerciseSettings> = _exerciseSettingsFlow2.asStateFlow()

    val isValidConfiguration: StateFlow<Boolean> = exerciseSettingsFlow2.map {
        hasNotePoolDegreesSelected() && hasSufficientRangeForPlayableGeneration()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = false,
    )

    fun saveExerciseSettings() {
        // todo look into an applicationScope implementation
        viewModelScope.launch {
            // todo save object in room, once refactored
            prefsStore.saveExerciseSettings(exerciseSettingsFlow2.value)
        }
    }

    fun saveChromaticDegrees(degrees: BooleanArray) {
        _exerciseSettingsFlow2.value = exerciseSettingsFlow2.value.copy(chromaticDegrees = degrees)
    }

    fun saveDiatonicDegrees(degrees: BooleanArray) {
        _exerciseSettingsFlow2.value = exerciseSettingsFlow2.value.copy(diatonicDegrees = degrees)
    }

    fun saveMatchOctave(matchOctave: Boolean) {
        _exerciseSettingsFlow2.value = exerciseSettingsFlow2.value.copy(matchOctave = matchOctave)
    }

    fun saveModeIx(ix: Int) {
        _exerciseSettingsFlow2.value = exerciseSettingsFlow2.value.copy(modeIx = ix)
    }

    fun saveNotePoolType(type: NotePoolType) {
        _exerciseSettingsFlow2.value = exerciseSettingsFlow2.value.copy(notePoolType = type)
    }

    fun saveNumQuestions(numQuestions: Int) {
        _exerciseSettingsFlow2.value = exerciseSettingsFlow2.value.copy(numQuestions = numQuestions)
    }

    fun saveParentScale(scale: ParentScale2) {
        _exerciseSettingsFlow2.value = exerciseSettingsFlow2.value.copy(parentScale = scale)
    }

    fun savePlayStartingPitch(playPitch: Boolean) {
        _exerciseSettingsFlow2.value = exerciseSettingsFlow2.value.copy(playStartingPitch = playPitch)
    }

    fun savePlayableLowerBound(lower: Note) {
        _exerciseSettingsFlow2.value = exerciseSettingsFlow2.value.copy(playableLowerBound = lower)
    }

    fun savePlayableUpperBound(upper: Note) {
        _exerciseSettingsFlow2.value = exerciseSettingsFlow2.value.copy(playableUpperBound = upper)
    }

    fun saveQuestionKey(key: PitchClass) {
        _exerciseSettingsFlow2.value = exerciseSettingsFlow2.value.copy(questionKey = key)
    }

    fun saveSessionTimeLimit(len: Int) {
        _exerciseSettingsFlow2.value = exerciseSettingsFlow2.value.copy(sessionTimeLimit = len)
    }

    fun saveSessionType(sessionType: SessionType) {
        _exerciseSettingsFlow2.value = exerciseSettingsFlow2.value.copy(sessionType = sessionType)
    }

    private fun hasNotePoolDegreesSelected(): Boolean {
        return when (exerciseSettingsFlow2.value.notePoolType) {
            NotePoolType.DIATONIC -> {
                exerciseSettingsFlow2.value.diatonicDegrees.contains(true)
            }
            NotePoolType.CHROMATIC -> {
                exerciseSettingsFlow2.value.chromaticDegrees.contains(true)
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
