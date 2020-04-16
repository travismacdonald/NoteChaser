package com.example.notechaser.data.exercisesetup


import androidx.lifecycle.MutableLiveData

class ExerciseSetupSettings {

    val playCadence = MutableLiveData<Boolean>()

    val matchKey = MutableLiveData<Boolean>()

    init {
        playCadence.value = true
        matchKey.value = true
    }

}