package com.example.notechaser.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import org.jetbrains.annotations.Nullable
import timber.log.Timber


@BindingAdapter("app:isGone")
fun setLayoutGone(view: View, @Nullable isVisible: LiveData<Boolean>) {
    Timber.d("view = ${view.id}; val = ${isVisible.value}")
    val params = view.layoutParams
    isVisible.value?.let {
        if (it) params.height = ViewGroup.LayoutParams.WRAP_CONTENT else params.height = 0
    }
}