package com.example.notechaser.data.exercisesetup

import android.view.View
import androidx.lifecycle.MutableLiveData

// TODO: Could implement a generated summary with all the values, but hey what do i kno
data class ExerciseSetupMultiList(
        val title: String,
        val summary: String,
        val entries: Array<String>,
        val itemChecked: MutableLiveData<BooleanArray>,
        val clickListener: View.OnClickListener,
        val isEnabled: MutableLiveData<Boolean> = MutableLiveData(true),
        val isVisible: MutableLiveData<Boolean> = MutableLiveData(true)
)