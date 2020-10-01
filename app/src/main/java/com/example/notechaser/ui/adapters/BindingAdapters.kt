package com.example.notechaser.ui.adapters

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.example.notechaser.data.exercisesetup.ExerciseSetupItem
import com.google.android.material.slider.Slider
import org.jetbrains.annotations.Nullable


@BindingAdapter("app:is_visible")
fun setLayoutGone(view: View, @Nullable isVisible: LiveData<Boolean>) {
    val params = view.layoutParams
    isVisible.value?.let {
        if (it) params.height = view.height else params.height = 0
    }
    view.layoutParams = params
}

@BindingAdapter("app:rangeBarValues")
fun setRangeBarValues(rangeBar: Slider, item: ExerciseSetupItem.RangeBar) {
    rangeBar.valueFrom = item.valueFrom
    rangeBar.valueTo = item.valueTo
    rangeBar.stepSize = item.stepSize
    rangeBar.setValues(item.lowerValue.value!!.toFloat(), item.upperValue.value!!.toFloat())
    rangeBar.addOnChangeListener { sliderItem, value, _ ->
        if (sliderItem.activeThumbIndex == 0) {
            item.lowerValue.value = value.toInt()
        }
        else {
            item.upperValue.value = value.toInt()
        }
    }
}