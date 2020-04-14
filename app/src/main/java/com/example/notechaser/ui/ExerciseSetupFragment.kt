package com.example.notechaser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notechaser.R
import com.example.notechaser.data.ExerciseSetupHeader
import com.example.notechaser.data.ExerciseSetupItem
import com.example.notechaser.data.ExerciseSetupSwitch
import com.example.notechaser.databinding.FragmentExerciseSetupBinding
import com.example.notechaser.utilities.InjectorUtils
import com.example.notechaser.viewmodels.ExerciseSetupViewModel
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class ExerciseSetupFragment : Fragment() {

    val viewModel: ExerciseSetupViewModel by viewModels {
        InjectorUtils.provideExerciseSetupViewModelFactory(
                ExerciseSetupFragmentArgs.fromBundle(arguments!!).exerciseType
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentExerciseSetupBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_exercise_setup, container,false
        )

        val manager = LinearLayoutManager(activity)
        val adapter = ExerciseSetupAdapter()

        val noteChoiceHeader: ExerciseSetupItem = ExerciseSetupItem.Header(ExerciseSetupHeader("note choice"))
        val playCadenceSwitch: ExerciseSetupItem = ExerciseSetupItem.Switch(ExerciseSetupSwitch("switch", false))
        val list: List<ExerciseSetupItem> = arrayListOf(noteChoiceHeader, playCadenceSwitch)
        adapter.submitList(list)

        binding.settingsList.adapter = adapter
        binding.settingsList.layoutManager = manager

        Timber.d("made it here")

        return binding.root
    }

}
