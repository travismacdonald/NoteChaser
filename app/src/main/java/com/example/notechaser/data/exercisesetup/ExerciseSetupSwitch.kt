package com.example.notechaser.data.exercisesetup

import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData

data class ExerciseSetupSwitch(
        val heading: String,
        val description: String,
        val isChecked: LiveData<Boolean>,
        val imgSrc: Int? = null) {

}