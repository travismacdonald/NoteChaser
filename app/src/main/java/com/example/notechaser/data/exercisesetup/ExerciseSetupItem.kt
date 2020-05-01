package com.example.notechaser.data.exercisesetup

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// TODO: Refactor this so all items are contained in this class
// TODO: perhaps this can be an interface instead
// TODO: implement equal and hashFunctions for classes with arrays
// TODO: make img source an interface field
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

    data class Switch(
            val title: String,
            val summary: String,
            val isChecked: MutableLiveData<Boolean>,
            override val isEnabled: LiveData<Boolean> = MutableLiveData(true),
            override val isVisible: LiveData<Boolean> = MutableLiveData(true),
            val imgSrc: Int? = null
    ) : ExerciseSetupItem {
        fun setIsChecked(value: Boolean) {
            isChecked.value = value
        }
    }

    data class Slider(val title: String,
                      val valueFrom: Float,
                      val valueTo: Float,
                      val value: MutableLiveData<Int>,
                      val displayValue: LiveData<String>,
                      val stepSize: Float = 1f,
                      override val isEnabled: LiveData<Boolean> = MutableLiveData(true),
                      override val isVisible: LiveData<Boolean> = MutableLiveData(true))
    : ExerciseSetupItem

    data class RangeBar(val title: String,
                        val valueFrom: Float,
                        val valueTo: Float,
                        val lowerValue: MutableLiveData<Int>,
                        val upperValue: MutableLiveData<Int>,
                        // TODO: See if possible to change to LiveData
                        val displayValue: MutableLiveData<String>,
                        val stepSize: Float = 1f,
                        override val isEnabled: LiveData<Boolean> = MutableLiveData(true),
                        override val isVisible: LiveData<Boolean> = MutableLiveData(true)
    ) : ExerciseSetupItem

}