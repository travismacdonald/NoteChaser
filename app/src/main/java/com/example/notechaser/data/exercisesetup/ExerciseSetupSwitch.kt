package com.example.notechaser.data.exercisesetup

import androidx.lifecycle.MutableLiveData

data class ExerciseSetupSwitch(
        val heading: String,
        val description: String,
        val isChecked: MutableLiveData<Boolean>,
        // TODO: find way to clean this up
        val isEnabled: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = true },
        val isVisible: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = true },
        val imgSrc: Int? = null) {

    fun setIsChecked(value: Boolean) {
        isChecked.value = value
    }

}