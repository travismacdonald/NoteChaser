package com.example.notechaser.data.exercisesetup

import androidx.lifecycle.MutableLiveData

data class ExerciseSetupSwitch(
        val heading: String,
        val description: String,
        val isChecked: MutableLiveData<Boolean>,
        val imgSrc: Int? = null) {

    fun setIsChecked(value: Boolean) {
        isChecked.value = value
    }

}