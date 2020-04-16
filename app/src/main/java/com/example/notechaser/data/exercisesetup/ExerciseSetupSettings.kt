package com.example.notechaser.data.exercisesetup


import androidx.lifecycle.MutableLiveData

class ExerciseSetupSettings {

    val playCadence = MutableLiveData<Boolean>()

    var matchKey: Boolean = false

    init {
        playCadence.value = true
    }

}