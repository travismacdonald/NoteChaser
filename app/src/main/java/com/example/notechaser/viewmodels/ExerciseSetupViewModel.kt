package com.example.notechaser.viewmodels

import androidx.lifecycle.ViewModel
import com.example.notechaser.data.ExerciseType
import com.example.notechaser.data.exercisesetup.ExerciseSetupSettings

class ExerciseSetupViewModel internal constructor(
        val exerciseType: ExerciseType
): ViewModel() {

    // TODO: Playable Generator
    val settings = ExerciseSetupSettings()


}