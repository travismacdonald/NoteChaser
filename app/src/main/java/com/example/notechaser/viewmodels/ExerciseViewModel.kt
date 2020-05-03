package com.example.notechaser.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.example.notechaser.playablegenerator.PlayableGenerator


class ExerciseViewModel internal constructor() : ViewModel() {

    val settings = ExerciseSetupSettings()

    lateinit var generator: PlayableGenerator

    val questionsAnswered = MutableLiveData(0)

}