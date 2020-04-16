package com.example.notechaser.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notechaser.data.exercisesetup.ExerciseSetupItem
import com.example.notechaser.data.exercisesetup.ExerciseSetupHeader
import com.example.notechaser.data.exercisesetup.ExerciseSetupSwitch
import com.example.notechaser.databinding.ItemExerciseSetupSwitchBinding
import com.example.notechaser.databinding.ItemSettingsHeaderBinding
import timber.log.Timber

import java.lang.ClassCastException

private val TYPE_HEADER = 0;
private val TYPE_SWITCH = 1;

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
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ExerciseSetupItem.Header -> TYPE_HEADER
            is ExerciseSetupItem.Switch -> TYPE_SWITCH
            is ExerciseSetupItem.SingleList -> -1 // TODO
            is ExerciseSetupItem.MultiList -> -1 // TODO
        }
    }

    class HeaderViewHolder private constructor(val binding: ItemSettingsHeaderBinding)
        : RecyclerView.ViewHolder(binding.root) {

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

    class SwitchViewHolder private constructor(val binding: ItemExerciseSetupSwitchBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(switch: ExerciseSetupSwitch) {
            binding.obj = switch
            if (switch.imgSrc != null) {
                binding.icon.setImageResource(switch.imgSrc)
            }
        }
        companion object {
            fun from(parent: ViewGroup): SwitchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemExerciseSetupSwitchBinding.inflate(layoutInflater, parent, false)
                return SwitchViewHolder(binding)
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