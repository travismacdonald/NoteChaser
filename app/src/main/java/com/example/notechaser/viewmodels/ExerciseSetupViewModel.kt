package com.example.notechaser.viewmodels

import androidx.lifecycle.ViewModel
import com.example.notechaser.data.ExerciseType
import com.example.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.example.notechaser.playablegenerator.Generator

class ExerciseSetupViewModel internal constructor(
        val exerciseType: ExerciseType
): ViewModel() {

    // TODO: Playable Generator
    val settings = ExerciseSetupSettings()
    val generator = Generator()



}