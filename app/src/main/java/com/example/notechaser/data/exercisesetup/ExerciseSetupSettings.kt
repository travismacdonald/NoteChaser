package com.example.notechaser.data.exercisesetup


import androidx.lifecycle.MutableLiveData
import com.example.notechaser.data.exercisesetup.Constants.NOTE_CHOICE_CHROMATIC

class ExerciseSetupSettings {

    val playCadence = MutableLiveData<Boolean>()

    val matchKey = MutableLiveData<Boolean>()

    val noteChoice = MutableLiveData(NOTE_CHOICE_CHROMATIC)

    init {
        // TODO: Move these values to constructor call
        playCadence.value = true
        matchKey.value = true
    }

}