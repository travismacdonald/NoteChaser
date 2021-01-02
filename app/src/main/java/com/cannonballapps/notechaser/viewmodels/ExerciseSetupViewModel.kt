package com.cannonballapps.notechaser.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cannonballapps.notechaser.data.ExerciseType
import com.cannonballapps.notechaser.data.NotePoolType
import com.cannonballapps.notechaser.data.ParentScale2
import com.cannonballapps.notechaser.data.SessionType
import com.cannonballapps.notechaser.prefsstore.PrefsStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ExerciseSetupViewModel(application: Application) : AndroidViewModel(application) {



    private val prefsStore = PrefsStore(application.applicationContext)

    lateinit var exerciseType: ExerciseType

    val chromaticDegrees = prefsStore.chromaticDegrees().asLiveData()
    val diatonicDegrees = prefsStore.diatonicDegrees().asLiveData()
    val matchOctave = prefsStore.matchOctave().asLiveData()
    val modeIx = prefsStore.modeIx().asLiveData()
    val notePoolType = prefsStore.notePoolType().asLiveData()
    val numQuestions = prefsStore.numQuestions().asLiveData()
    val parentScale = prefsStore.parentScale().asLiveData()
    val playableLowerBound = prefsStore.playableLowerBound().asLiveData()
    val playableUpperBound = prefsStore.playableUpperBound().asLiveData()
    val questionKey = prefsStore.questionKey().asLiveData()
    val sessionTimeLimit = prefsStore.sessionTimeLimit().asLiveData()
    val sessionType = prefsStore.sessionType().asLiveData()

    // TODO: remove some repeated logic
    val scaleName = MediatorLiveData<String>().apply {
        addSource(parentScale) { parentScale ->
            modeIx.value?.let { modeIx ->
                this.value = parentScale.modeNames[modeIx]
            }
        }
        addSource(modeIx) { modeIx ->
            parentScale.value?.let { parentScale ->
                this.value = parentScale.modeNames[modeIx]
            }
        }
    }

    val playableBounds = MediatorLiveData<Pair<Int, Int>>().apply {
        addSource(playableLowerBound) { lowerBound ->
            playableUpperBound.value?.let { upperBound ->
                updatePlayableBounds(lowerBound, upperBound)
            }
        }
        addSource(playableUpperBound) { upperBound ->
            playableLowerBound.value?.let { lowerBound ->
                updatePlayableBounds(lowerBound, upperBound)
            }
        }
    }

    // TODO: clean up; separate hasNotePoolDegrees into func for diatonic and chromatic
    val isValidConfiguration = MediatorLiveData<Boolean>().apply {
        addSource(notePoolType) {
            this.value = hasNotePoolDegreesSelected()
        }
        addSource(chromaticDegrees) {
            this.value = hasNotePoolDegreesSelected()
        }
        addSource(diatonicDegrees) {
            this.value = hasNotePoolDegreesSelected()
        }

        addSource(playableBounds) { bounds ->
            this.value = hasSufficientRangeForPlayableGeneration()
        }
    }

    init {
        prefetchPrefsStore()
    }

    private fun prefetchPrefsStore() {
        viewModelScope.launch {
            prefsStore.chromaticDegrees().first()
            prefsStore.diatonicDegrees().first()
            prefsStore.matchOctave().first()
            prefsStore.modeIx().first()
            prefsStore.notePoolType().first()
            prefsStore.parentScale().first()
            prefsStore.playableLowerBound().first()
            prefsStore.playableUpperBound().first()
            prefsStore.questionKey().first()
            prefsStore.sessionTimeLimit().first()
            prefsStore.sessionType().first()
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

    fun savePlayableLowerBound(ix: Int) {
        viewModelScope.launch {
            prefsStore.savePlayableLowerBound(ix)
        }
    }

    fun savePlayableUpperBound(ix: Int) {
        viewModelScope.launch {
            prefsStore.savePlayableUpperBound(ix)
        }
    }

    fun saveQuestionKey(key: Int) {
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

    private fun updatePlayableBounds(lower: Int, upper: Int) {
        if (lower != playableBounds.value?.first || upper != playableBounds.value?.second) {
            playableBounds.value = Pair(lower, upper)
        }
    }

    private fun hasNotePoolDegreesSelected(): Boolean {
        return when (notePoolType.value) {
            NotePoolType.DIATONIC -> {
                diatonicDegrees.value?.contains(true) ?: false
            }
            NotePoolType.CHROMATIC -> {
                chromaticDegrees.value?.contains(true) ?: false
            }
            else -> throw IllegalStateException(
                    "Illegal NotePoolType given: ${notePoolType.value}"
            )
        }
    }

    private fun hasSufficientRangeForPlayableGeneration(): Boolean {

        /*
         * Variables that Depends on:
         *   Key,
         *   Intervals,
         *   Lower bound,
         *   Upper bound,
         *
         * Info used to calculate:
         *   Range of pattern (i.e. highest - lowest) (e.g. single note playables
         *  will be 0)
         *
         *
         */


        if (notePoolType.value == NotePoolType.CHROMATIC) {

        }
        return true
    }

}