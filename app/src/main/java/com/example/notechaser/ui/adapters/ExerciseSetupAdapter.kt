package com.example.notechaser.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notechaser.data.exercisesetup.*
import com.example.notechaser.databinding.*
import timber.log.Timber

// TODO: fix gap here
private const val TYPE_HEADER = 0
private const val TYPE_SWITCH = 1

private const val TYPE_SINGLE_LIST = 3
private const val TYPE_MULTI_LIST = 4
private const val TYPE_SLIDER = 5
private const val TYPE_RANGE_BAR = 6

// TODO: perhaps? save attribute to shared preferences on every attribute change

class ExerciseSetupAdapter(private val lifecycleOwner: LifecycleOwner) :
        ListAdapter<ExerciseSetupItem, RecyclerView.ViewHolder>(ExerciseSettingDiffCallback()) {

    override fun getItemViewType(position: Int): Int {

        return when (getItem(position)) {
            is ExerciseSetupItem.Header -> TYPE_HEADER
//            is ExerciseSetupItem.Switch -> TYPE_SWITCH
            is ExerciseSetupItem.SingleList -> TYPE_SINGLE_LIST
            is ExerciseSetupItem.MultiList -> TYPE_MULTI_LIST
            is ExerciseSetupItem.Slider -> TYPE_SLIDER
            is ExerciseSetupItem.RangeBar -> TYPE_RANGE_BAR
            else -> -1
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
            TYPE_SINGLE_LIST -> SingleListViewHolder.from(parent)
            TYPE_MULTI_LIST -> MultiListViewHolder.from(parent)
            TYPE_SLIDER -> SliderViewHolder.from(parent)
            TYPE_RANGE_BAR -> RangeBarViewHolder.from(parent)
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                val headerItem = getItem(position) as ExerciseSetupItem.Header
                holder.bind(headerItem.header)
            }
//            is SwitchViewHolder -> {
//                val switchItem = getItem(position) as ExerciseSetupItem.Switch
//                holder.bind(switchItem.switch)
//            }
            is SingleListViewHolder -> {
                val listItem = getItem(position) as ExerciseSetupItem.SingleList
                holder.bind(listItem)
                holder.binding.lifecycleOwner = lifecycleOwner
            }
            is MultiListViewHolder -> {
                val listItem = getItem(position) as ExerciseSetupItem.MultiList
                holder.bind(listItem)
                holder.binding.lifecycleOwner = lifecycleOwner
            }
            is SliderViewHolder -> {
                val sliderItem = getItem(position) as ExerciseSetupItem.Slider
                holder.bind(sliderItem)
                holder.binding.lifecycleOwner = lifecycleOwner
            }
            is RangeBarViewHolder -> {
                val rangeBarItem = getItem(position) as ExerciseSetupItem.RangeBar
                holder.bind(rangeBarItem)
                holder.binding.lifecycleOwner = lifecycleOwner
            }
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

    /**
     * Setup Single List
     */
    class SingleListViewHolder private constructor(val binding: ItemSettingsSingleListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(list: ExerciseSetupItem.SingleList) {
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
     * Setup Multi List
     */
    class MultiListViewHolder private constructor(val binding: ItemSettingsMultiListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(list: ExerciseSetupItem.MultiList) {
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

    /**
     * Setup Slider
     */
    class SliderViewHolder private constructor(val binding: ItemSettingsSliderBinding)
        : RecyclerView.ViewHolder(binding.root) {

        // TODO: Move to databinding if you can
        // TODO: Change some of the naming conventions: kind of confusing
        fun bind(slider: ExerciseSetupItem.Slider) {
            binding.obj = slider
            binding.slider.valueFrom = slider.valueFrom
            binding.slider.valueTo = slider.valueTo
            binding.slider.stepSize = slider.stepSize
            binding.slider.value = slider.value.value!!.toFloat()
            binding.slider.addOnChangeListener { _, value, _ ->
                slider.value.value = value.toInt()
            }

        }
        companion object {
            fun from(parent: ViewGroup): SliderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSettingsSliderBinding.inflate(layoutInflater, parent, false)
                return SliderViewHolder(binding)
            }
        }
    }

    /**
     * Setup RangeBar
     */
    class RangeBarViewHolder private constructor(val binding: ItemSettingsRangeBarBinding)
        : RecyclerView.ViewHolder(binding.root) {

        // TODO: Move to databinding if you can
        // TODO: Change some of the naming conventions: kind of confusing
        fun bind(rangeBar: ExerciseSetupItem.RangeBar) {
            binding.obj = rangeBar
            binding.rangeBar.valueFrom = rangeBar.valueFrom
            binding.rangeBar.valueTo = rangeBar.valueTo
            binding.rangeBar.stepSize = rangeBar.stepSize
            binding.rangeBar.setValues(
                    rangeBar.lowerValue.value!!.toFloat(), rangeBar.upperValue.value!!.toFloat())
            binding.rangeBar.addOnChangeListener { sliderItem, value, fromUser ->
                if (sliderItem.activeThumbIndex == 0) {
                    rangeBar.lowerValue.value = value.toInt()
                }
                else {
                    rangeBar.upperValue.value = value.toInt()
                }
            }

        }
        companion object {
            fun from(parent: ViewGroup): RangeBarViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSettingsRangeBarBinding.inflate(layoutInflater, parent, false)
                return RangeBarViewHolder(binding)
            }
        }
    }

}

// TODO: Probably get rid of this - contents don't change in this adapter
private class ExerciseSettingDiffCallback : DiffUtil.ItemCallback<ExerciseSetupItem>() {

    // TODO: fix this later
    override fun areContentsTheSame(oldItem: ExerciseSetupItem, newItem: ExerciseSetupItem): Boolean {
        return false
//        return oldItem == newItem
    }

    // TODO: fix this later
    override fun areItemsTheSame(oldItem: ExerciseSetupItem, newItem: ExerciseSetupItem): Boolean {
        return false // TODO fix
    }

}