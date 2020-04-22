package com.example.notechaser.data.exercisesetup

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData

class ExerciseSetupSlider(
        val title: String,
        val valueFrom: Float,
        val valueTo: Float,
        val curValue: MutableLiveData<Int>,
        val stepSize: Float = 1f,
        val displayValues: Array<String>? = null
)