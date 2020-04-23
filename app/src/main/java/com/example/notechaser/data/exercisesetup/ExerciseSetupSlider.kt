package com.example.notechaser.data.exercisesetup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ExerciseSetupSlider(
        val title: String,
        val valueFrom: Float,
        val valueTo: Float,
        val value: MutableLiveData<Int>,
        val displayValue: LiveData<String>,
        val stepSize: Float = 1f,
        val isEnabled: MutableLiveData<Boolean> = MutableLiveData(true),
        val isVisible: MutableLiveData<Boolean> = MutableLiveData(true)
)