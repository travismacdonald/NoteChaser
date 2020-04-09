package com.example.notechaser.ui.exerciseselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.notechaser.R
import com.example.notechaser.data.ExerciseType
import com.example.notechaser.databinding.FragmentExerciseSelectionBinding

class ExerciseSelectionFragment : Fragment() {

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
            customButton.setOnClickListener {view ->
                view.findNavController().navigate(
                        ExerciseSelectionFragmentDirections
                                .actionExerciseSelectionFragmentToExerciseConfigurationFragment(ExerciseType.CUSTOM))
            }
        }

        return binding.root
    }

}