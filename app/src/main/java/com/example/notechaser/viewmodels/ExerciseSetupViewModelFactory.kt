package com.example.notechaser.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notechaser.data.ExerciseType

class ExerciseSetupViewModelFactory(
        private val exerciseType: ExerciseType
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return ExerciseSetupViewModel(exerciseType) as T
    }
}