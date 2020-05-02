package com.example.notechaser.viewmodels

import androidx.lifecycle.ViewModel
import com.example.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.example.notechaser.playablegenerator.PlayableGenerator


class ExerciseSetupViewModel internal constructor() : ViewModel() {

    val settings = ExerciseSetupSettings()

    lateinit var generator: PlayableGenerator

}