package com.example.notechaser.ui.adapters

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.slider.Slider

@BindingAdapter("onValueChange")
fun bindOnValueChange(slider: Slider, newValue: Int) {
    slider.value = newValue.toFloat()
}