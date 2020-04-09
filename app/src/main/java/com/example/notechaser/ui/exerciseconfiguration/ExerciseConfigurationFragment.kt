package com.example.notechaser.ui.exerciseconfiguration


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.notechaser.R
import com.example.notechaser.databinding.FragmentExerciseConfigurationBinding

class ExerciseConfigurationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentExerciseConfigurationBinding =
                DataBindingUtil.inflate(
                        inflater,
                        R.layout.fragment_exercise_configuration,
                        container,
                        false)
        val args = ExerciseConfigurationFragmentArgs.fromBundle(arguments!!)
        Log.d("fragx", args.exerciseType.toString());

        return binding.root
    }
}