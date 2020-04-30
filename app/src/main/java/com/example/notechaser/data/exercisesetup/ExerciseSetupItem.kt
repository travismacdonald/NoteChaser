package com.example.notechaser.data.exercisesetup

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// TODO: Refactor this so all items are contained in this class
// TODO: perhaps this can be an interface instead
interface ExerciseSetupItem {

    val isEnabled: LiveData<Boolean>

    val isVisible: LiveData<Boolean>

    data class Header(
            val header: ExerciseSetupHeader,
            override val isEnabled: LiveData<Boolean> = MutableLiveData(true),
            override val isVisible: LiveData<Boolean> = MutableLiveData(true)
    ) : ExerciseSetupItem

    data class SingleList(
            val title: String,
            val entries: Array<String>,
            val itemChecked: LiveData<Int>,
            val clickListener: View.OnClickListener,
            override val isEnabled: LiveData<Boolean> = MutableLiveData(true),
            override val isVisible: LiveData<Boolean> = MutableLiveData(true)
    ) : ExerciseSetupItem

    data class MultiList(
            val title: String,
            val summary: String,
            val entries: Array<String>,
            val itemsChecked: LiveData<BooleanArray>,
            val clickListener: View.OnClickListener,
            override val isEnabled: LiveData<Boolean> = MutableLiveData(true),
            override val isVisible: LiveData<Boolean> = MutableLiveData(true)
    ) : ExerciseSetupItem

//    data class Switch(val switch: ExerciseSetupSwitch) : ExerciseSetupItem()
//
//    data class Slider(val slider: ExerciseSetupSlider) : ExerciseSetupItem()
//
//    data class RangeBar(val rangeBar: ExerciseSetupRangeBar) : ExerciseSetupItem()

}