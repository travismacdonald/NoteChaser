package com.example.notechaser.data.exercisesetup


import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.example.notechaser.data.exercisesetup.Constants.NOTE_CHOICE_CHROMATIC

class ExerciseSetupSettings {

    val playCadence = MutableLiveData<Boolean>()

    val matchKey = MutableLiveData<Boolean>()

    val noteChoice = MutableLiveData(NOTE_CHOICE_CHROMATIC)

    // 1. Ascending; 2. Descending;
    val playbackTypeMel = MutableLiveData(booleanArrayOf(true, false))

    // 1. Harmonic; 2. Ascending; 3. Descending;
    val playbackTypeHar = MutableLiveData(booleanArrayOf(true, false, false))

    val numQuestions = MutableLiveData(20)

    val maxIntervalInPattern = MutableLiveData(4)

    init {
        // TODO: Move these values to constructor call
        playCadence.value = true
        matchKey.value = true
    }

}