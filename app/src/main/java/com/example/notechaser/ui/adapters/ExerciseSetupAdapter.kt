package com.example.notechaser.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notechaser.data.exercisesetup.ExerciseSetupItem
import com.example.notechaser.data.exercisesetup.ExerciseSetupHeader
import com.example.notechaser.data.exercisesetup.ExerciseSetupSpinner
import com.example.notechaser.data.exercisesetup.ExerciseSetupSwitch

import com.example.notechaser.databinding.ItemSettingsHeaderBinding
import com.example.notechaser.databinding.ItemSettingsSpinnerBinding
import com.example.notechaser.databinding.ItemSettingsSwitchBinding

import java.lang.ClassCastException

private const val TYPE_HEADER = 0
private const val TYPE_SWITCH = 1
private const val TYPE_SPINNER = 2

// TODO: perhaps? save attribute to shared preferences on every attribute change

class ExerciseSetupAdapter(private val lifecycleOwner: LifecycleOwner) :
        ListAdapter<ExerciseSetupItem, RecyclerView.ViewHolder>(ExerciseSettingDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                val headerItem = getItem(position) as ExerciseSetupItem.Header
                holder.bind(headerItem.header)
            }
            is SwitchViewHolder -> {
                val switchItem = getItem(position) as ExerciseSetupItem.Switch
                holder.bind(switchItem.switch)
            }
            is SpinnerViewHolder -> {
                val spinnerItem = getItem(position) as ExerciseSetupItem.Spinner
                holder.bind(spinnerItem.spinner)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER ->
                HeaderViewHolder.from(parent).apply {
                    binding.lifecycleOwner = lifecycleOwner
                }
            TYPE_SWITCH ->
                SwitchViewHolder.from(parent).apply {
                    binding.lifecycleOwner = lifecycleOwner
                }
            TYPE_SPINNER ->
                SpinnerViewHolder.from(parent).apply {
                    binding.lifecycleOwner = lifecycleOwner
                }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ExerciseSetupItem.Header -> TYPE_HEADER
            is ExerciseSetupItem.Switch -> TYPE_SWITCH
            is ExerciseSetupItem.Spinner -> TYPE_SPINNER
            is ExerciseSetupItem.SingleList -> -1 // TODO
            is ExerciseSetupItem.MultiList -> -1 // TODO
        }
    }

    /**
     * Setup Header
     */
    class HeaderViewHolder private constructor(val binding: ItemSettingsHeaderBinding)
        : RecyclerView.ViewHolder(binding.root) {

        // TODO: Clean this up
        fun bind(header: ExerciseSetupHeader) {
            binding.textview.text = header.text
        }
        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSettingsHeaderBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }


    /**
     * Setup Item Switch
     */
    class SwitchViewHolder private constructor(val binding: ItemSettingsSwitchBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(switch: ExerciseSetupSwitch) {
            binding.obj = switch
            // TODO: Clean this up
            switch.imgSrc?.let { binding.icon.setImageResource(switch.imgSrc) }
        }
        companion object {
            fun from(parent: ViewGroup): SwitchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSettingsSwitchBinding.inflate(layoutInflater, parent, false)
                return SwitchViewHolder(binding)
            }
        }
    }

    class SpinnerViewHolder private constructor(val binding: ItemSettingsSpinnerBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(spinner: ExerciseSetupSpinner) {
            binding.obj = spinner
        }
        companion object {
            fun from(parent: ViewGroup): SpinnerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSettingsSpinnerBinding.inflate(layoutInflater, parent, false)
                return SpinnerViewHolder(binding)
            }
        }
    }
}

// TODO: Verify correctness
private class ExerciseSettingDiffCallback : DiffUtil.ItemCallback<ExerciseSetupItem>() {

    override fun areContentsTheSame(oldItem: ExerciseSetupItem, newItem: ExerciseSetupItem): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: ExerciseSetupItem, newItem: ExerciseSetupItem): Boolean {
        return false // TODO fix
    }

}