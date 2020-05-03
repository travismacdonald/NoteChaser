package com.example.notechaser.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.example.notechaser.playablegenerator.PlayableGenerator


class ExerciseViewModel internal constructor() : ViewModel() {

    val settings = ExerciseSetupSettings()

    lateinit var generator: PlayableGenerator

    val questionsAnswered = MutableLiveData(0)

    val secondsPassed = MutableLiveData<Long>()

    lateinit var timer: CountDownTimer

}