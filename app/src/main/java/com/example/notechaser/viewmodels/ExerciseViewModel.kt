package com.example.notechaser.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.example.notechaser.playablegenerator.Playable
import com.example.notechaser.playablegenerator.PlayableGenerator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ExerciseViewModel internal constructor() : ViewModel() {

    val settings = ExerciseSetupSettings()

    lateinit var generator: PlayableGenerator

    val questionsAnswered = MutableLiveData(0)

    val secondsPassed = MutableLiveData<Int>()

    val currentPlayable = MutableLiveData<Playable?>()

    private var timerUpdate: Job? = null

    fun startTimer() {
        secondsPassed.value = 0
        timerUpdate = viewModelScope.launch {
            while(secondsPassed.value!! < settings.timerLength.value!! * 60) {
                delay(1000)
                secondsPassed.value = secondsPassed.value!! + 1
            }
        }
    }

    fun generatePlayable() {
        currentPlayable.value = generator.generatePlayable()
    }

}