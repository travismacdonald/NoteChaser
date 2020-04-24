package com.example.notechaser.data.exercisesetup

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class ExerciseSetupSingleList(
        val title: String,
        val entries: Array<String>,
        val itemChecked: LiveData<Int>,
        val clickListener: View.OnClickListener,
        val isEnabled: MutableLiveData<Boolean> = MutableLiveData(true),
        val isVisible: MutableLiveData<Boolean> = MutableLiveData(true)
)