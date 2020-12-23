package com.cannonballapps.notechaser

import androidx.databinding.BindingAdapter
import com.cannonballapps.notechaser.data.exercisesetup.ExerciseSetupItem
import com.google.android.material.slider.Slider


//@BindingAdapter("app:is_visible")
//fun setLayoutGone(view: View, @Nullable isVisible: LiveData<Boolean>) {
//    val params = view.layoutParams
//    isVisible.value?.let {
//        if (it) params.height = view.height else params.height = 0
//    }
//    view.layoutParams = params
//}

@BindingAdapter("app:nc_rangeBarValues")
fun setRangeBarValues(rangeBar: Slider, item: ExerciseSetupItem.RangeBar) {
//    rangeBar.valueFrom = item.valueFrom
//    rangeBar.valueTo = item.valueTo
//    rangeBar.stepSize = item.stepSize
//    rangeBar.setValues(item.lowerValue.value!!.toFloat(), item.upperValue.value!!.toFloat())
//    rangeBar.addOnChangeListener { sliderItem, value, _ ->
//        if (sliderItem.activeThumbIndex == 0) {
//            item.lowerValue.value = value.toInt()
//        }
//        else {
//            item.upperValue.value = value.toInt()
//        }
//    }
}

@BindingAdapter("app:nc_sliderValues")
fun setSliderValues(slider: Slider, item: ExerciseSetupItem.Slider) {
//    slider.valueFrom = item.valueFrom
//    slider.valueTo = item.valueTo
//    slider.stepSize = item.stepSize
    item.value.value?.let {
        slider.value = it.toFloat()
//        slider.value = item.value.value!!.toFloat()
    }
//    if (item.value.value == null) {
//        slider.value = item.valueFrom
//    }
//    slider.addOnChangeListener { _, value, _ ->
//        item.value.value = value.toInt()
//    }
}