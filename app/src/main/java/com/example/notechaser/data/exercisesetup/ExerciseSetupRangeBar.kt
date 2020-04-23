package com.example.notechaser.data.exercisesetup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// TODO: Can probably use less code by inheritance
class ExerciseSetupRangeBar(
        val title: String,
        val valueFrom: Float,
        val valueTo: Float,
        val lowerValue: MutableLiveData<Int>,
        val upperValue: MutableLiveData<Int>,
        // TODO: See if possible to change to LiveData
        val displayValue: MutableLiveData<String>,
        val stepSize: Float = 1f,
        val isEnabled: MutableLiveData<Boolean> = MutableLiveData(true),
        val isVisible: MutableLiveData<Boolean> = MutableLiveData(true)
)