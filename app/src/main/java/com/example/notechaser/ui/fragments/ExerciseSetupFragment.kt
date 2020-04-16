package com.example.notechaser.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notechaser.R
import com.example.notechaser.data.exercisesetup.ExerciseSetupHeader
import com.example.notechaser.data.exercisesetup.ExerciseSetupItem
import com.example.notechaser.data.exercisesetup.ExerciseSetupSwitch
import com.example.notechaser.databinding.FragmentExerciseSetupBinding
import com.example.notechaser.ui.adapters.ExerciseSetupAdapter
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

    lateinit var binding: FragmentExerciseSetupBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_exercise_setup, container,false
        )
        binding.lifecycleOwner = this

        val manager = LinearLayoutManager(activity)
        val adapter = ExerciseSetupAdapter()

        val noteChoiceHeader: ExerciseSetupItem = ExerciseSetupItem.Header(ExerciseSetupHeader("note choice"))
        val playCadenceSwitch: ExerciseSetupItem =
                ExerciseSetupItem.Switch(ExerciseSetupSwitch(
                        "Play Cadence",
                        "Play cadence before question",
                        viewModel.settings.playCadence)
                )

        viewModel.settings.playCadence.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })

        val list: List<ExerciseSetupItem> = arrayListOf(noteChoiceHeader, playCadenceSwitch)
        adapter.submitList(list)

        binding.settingsList.adapter = adapter
        binding.settingsList.layoutManager = manager
        binding.button.setOnClickListener { changePlayCadence() }



        Timber.d("made it here")

        return binding.root
    }

    private fun changePlayCadence() {
        viewModel.settings.playCadence.value = !viewModel.settings.playCadence.value!!
        binding.button.text = viewModel.settings.playCadence.value.toString()
    }

}
