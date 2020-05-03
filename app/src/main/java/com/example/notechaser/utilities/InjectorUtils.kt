package com.example.notechaser.utilities

import com.example.notechaser.viewmodels.ExerciseViewModelFactory

object InjectorUtils {

    fun provideExerciseSetupViewModelFactory(): ExerciseViewModelFactory {
        return ExerciseViewModelFactory()
    }

}