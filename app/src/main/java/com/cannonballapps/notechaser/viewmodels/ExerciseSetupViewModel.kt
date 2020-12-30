package com.cannonballapps.notechaser.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cannonballapps.notechaser.data.NotePoolType
import com.cannonballapps.notechaser.data.ParentScale2
import com.cannonballapps.notechaser.data.SessionType
import com.cannonballapps.notechaser.prefsstore.PrefsStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class ExerciseSetupViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsStore = PrefsStore(application.applicationContext)

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
        fun updateBounds(lower: Int, upper: Int) {
            if (lower != this.value?.first || upper != this.value?.second) {
                this.value = Pair(lower, upper)
            }
        }
        addSource(playableLowerBound) { lowerBound ->
            playableUpperBound.value?.let { upperBound ->
                updateBounds(lowerBound, upperBound)
            }
        }
        addSource(playableUpperBound) { upperBound ->
            playableLowerBound.value?.let { lowerBound ->
                updateBounds(lowerBound, upperBound)
            }
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

}