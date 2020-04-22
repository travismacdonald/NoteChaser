package com.example.notechaser.data.exercisesetup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// todo: add enabled / visibility components
class ExerciseSetupSlider(
        val title: String,
        val valueFrom: Float,
        val valueTo: Float,
        val curValue: MutableLiveData<Int>,
        val curDisplayValue: LiveData<String>,
        val stepSize: Float = 1f,
        val displayValues: Array<String>? = null
)