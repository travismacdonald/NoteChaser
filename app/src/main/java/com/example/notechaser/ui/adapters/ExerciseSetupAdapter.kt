package com.example.notechaser.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notechaser.data.exercisesetup.*
import com.example.notechaser.databinding.*

import java.lang.ClassCastException

private const val TYPE_HEADER = 0
private const val TYPE_SWITCH = 1
private const val TYPE_SPINNER = 2
private const val TYPE_SINGLE_LIST = 3
private const val TYPE_MULTI_LIST = 4

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
            is SingleListViewHolder -> {
                val listItem = getItem(position) as ExerciseSetupItem.SingleList
                holder.bind(listItem.list)
            }
            is MultiListViewHolder -> {
                val listItem = getItem(position) as ExerciseSetupItem.MultiList
                holder.bind(listItem.list)
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
            TYPE_SINGLE_LIST ->
                SingleListViewHolder.from(parent).apply {
                    binding.lifecycleOwner = lifecycleOwner
                }
            TYPE_MULTI_LIST ->
                MultiListViewHolder.from(parent).apply {
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
            is ExerciseSetupItem.SingleList -> TYPE_SINGLE_LIST
            is ExerciseSetupItem.MultiList -> TYPE_MULTI_LIST
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

    /**
     * Setup Single List
     */
    class SingleListViewHolder private constructor(val binding: ItemSettingsSingleListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(list: ExerciseSetupSingleList) {
            binding.obj = list
        }
        companion object {
            fun from(parent: ViewGroup): SingleListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSettingsSingleListBinding.inflate(layoutInflater, parent, false)
                return SingleListViewHolder(binding)
            }
        }
    }

    /**
     * Setup Single List
     */
    class MultiListViewHolder private constructor(val binding: ItemSettingsMultiListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(list: ExerciseSetupMultiList) {
            binding.obj = list
        }
        companion object {
            fun from(parent: ViewGroup): MultiListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSettingsMultiListBinding.inflate(layoutInflater, parent, false)
                return MultiListViewHolder(binding)
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