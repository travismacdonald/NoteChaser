package com.example.notechaser.utilities

import com.example.notechaser.data.ExerciseType
import com.example.notechaser.viewmodels.ExerciseSetupViewModel
import com.example.notechaser.viewmodels.ExerciseSetupViewModelFactory

object InjectorUtils {

    fun provideExerciseSetupViewModelFactory(
            exerciseType: ExerciseType
    ): ExerciseSetupViewModelFactory {
        return ExerciseSetupViewModelFactory(exerciseType)
    }

}