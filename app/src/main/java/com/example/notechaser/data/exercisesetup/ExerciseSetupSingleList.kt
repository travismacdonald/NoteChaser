package com.example.notechaser.data.exercisesetup

import android.view.View
import androidx.lifecycle.MutableLiveData


data class ExerciseSetupSingleList(
        val title: String,
        val summary: String,
        val entries: Array<String>,
        val itemChecked: MutableLiveData<Int>,
        val clickListener: View.OnClickListener,
        val isEnabled: MutableLiveData<Boolean> = MutableLiveData(true),
        val isVisible: MutableLiveData<Boolean> = MutableLiveData(true)
)