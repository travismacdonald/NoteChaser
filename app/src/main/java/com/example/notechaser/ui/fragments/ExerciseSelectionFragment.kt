package com.example.notechaser.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.notechaser.R
import com.example.notechaser.data.ExerciseType
import com.example.notechaser.databinding.FragmentExerciseSelectionBinding
import com.example.notechaser.viewmodels.ExerciseViewModel


class ExerciseSelectionFragment : Fragment() {

    // TODO: try to figure out a way to init the viewmodel when this activity starts
    private val viewModel: ExerciseViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentExerciseSelectionBinding =
                DataBindingUtil.inflate(
                        inflater,
                        R.layout.fragment_exercise_selection,
                        container,
                        false)


        binding.apply {
            singlenoteButton.setOnClickListener { view ->
                navToExerciseSelectionFragment(view, ExerciseType.SINGLE_NOTE)
            }
            intervallicButton.setOnClickListener { view ->
                navToExerciseSelectionFragment(view, ExerciseType.INTERVALLIC)
            }
            harmonicButton.setOnClickListener { view ->
                navToExerciseSelectionFragment(view, ExerciseType.HARMONIC)
            }
            scaleButton.setOnClickListener { view ->
                navToExerciseSelectionFragment(view, ExerciseType.SCALE)
            }
            melodicButton.setOnClickListener { view ->
                navToExerciseSelectionFragment(view, ExerciseType.MELODIC)
            }
            customButton.setOnClickListener { view ->
                navToExerciseSelectionFragment(view, ExerciseType.CUSTOM)
            }
        }


        return binding.root
    }

    private fun navToExerciseSelectionFragment(view: View, type: ExerciseType) {
        val directions = ExerciseSelectionFragmentDirections.actionExerciseSelectionFragmentToExerciseSetupFragment(type)
        view.findNavController().navigate(directions)
    }

}