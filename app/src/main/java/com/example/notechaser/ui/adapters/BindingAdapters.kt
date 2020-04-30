package com.example.notechaser.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import org.jetbrains.annotations.Nullable


@BindingAdapter("app:is_visible")
fun setLayoutGone(view: View, @Nullable isVisible: LiveData<Boolean>) {
//    Timber.d("view = ${view.title.text}; val = ${isVisible.value}; oldH = ${view.height}")
    val params = view.layoutParams
    isVisible.value?.let {
        if (it) params.height = ViewGroup.LayoutParams.WRAP_CONTENT else params.height = 0
    }
    view.layoutParams = params
//    Timber.d("view = ${view.title.text}; val = ${isVisible.value}; newH = ${view.height}")
}