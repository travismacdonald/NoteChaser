package com.example.notechaser.data.exercisesetup


import androidx.lifecycle.MutableLiveData

class ExerciseSetupSettings {

    val playCadence = MutableLiveData<Boolean>()

    val matchKey = MutableLiveData<Boolean>()

    val sessionLengthType = MutableLiveData(QUESTION_LIMIT)

    val matchOctave = MutableLiveData(false)

    // 1. Ascending; 2. Descending;
    val playbackTypeMel = MutableLiveData(booleanArrayOf(true, false))

    // 1. Harmonic; 2. Ascending; 3. Descending;
    val playbackTypeHar = MutableLiveData(booleanArrayOf(true, false, false))

    val numQuestions = MutableLiveData(20)

    val timerLength = MutableLiveData(10)

    val maxIntervalInPattern = MutableLiveData(4)

    init {
        // TODO: Move these values to constructor call
        playCadence.value = true
        matchKey.value = true
    }

    companion object {
        const val QUESTION_LIMIT = 0
        const val TIME_LIMIT = 1
        const val UNLIMITED = 2
    }

}