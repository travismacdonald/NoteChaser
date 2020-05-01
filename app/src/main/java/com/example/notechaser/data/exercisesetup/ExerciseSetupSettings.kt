package com.example.notechaser.data.exercisesetup


import androidx.lifecycle.MutableLiveData

class ExerciseSetupSettings {

    val playCadence = MutableLiveData<Boolean>()

    val matchKey = MutableLiveData<Boolean>()

    val sessionLength = MutableLiveData(QUESTION_LIMIT)

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

    companion object {
        val QUESTION_LIMIT = 0
        val TIME_LIMIT = 1
        val UNLIMITED = 2
    }

}