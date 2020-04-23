package com.example.notechaser.data.exercisesetup

import androidx.lifecycle.MutableLiveData

data class ExerciseSetupSwitch(
        val title: String,
        val summary: String,
        val isChecked: MutableLiveData<Boolean>,
        val isEnabled: MutableLiveData<Boolean> = MutableLiveData(true),
        val isVisible: MutableLiveData<Boolean> = MutableLiveData(true),
        val imgSrc: Int? = null) {

    fun setIsChecked(value: Boolean) {
        isChecked.value = value
    }

}