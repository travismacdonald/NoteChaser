package com.example.notechaser.data.exercisesetup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ExerciseSetupSettings {

    val playCadence = MutableLiveData<Boolean>()
//    val playCadence: LiveData<Boolean>
//        get() = _playCadence


    var matchKey: Boolean = false

    init {
        playCadence.value = true
    }

}