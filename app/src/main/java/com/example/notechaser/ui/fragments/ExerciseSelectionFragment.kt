package com.example.notechaser.ui.fragments

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.notechaser.R
import com.example.notechaser.data.ExerciseType
import com.example.notechaser.databinding.FragmentExerciseSelectionBinding
import com.example.notechaser.viewmodels.ExerciseViewModel


class ExerciseSelectionFragment : Fragment() {

    private val MICROPHONE_PERMISSION_CODE = 1


    // TODO: try to figure out a way to init the viewmodel when this activity starts
    private val viewModel: ExerciseViewModel by viewModels()

    private lateinit var binding: FragmentExerciseSelectionBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_exercise_selection,
                container,
                false)


        if (!hasMicrophoneRuntimePermission()) {
            requestMicrophonePermission()
        }

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

    private fun hasMicrophoneRuntimePermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
                binding.getRoot().getContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED)
    }

    // todo: maybe find a way to clean this up; extract hardcoded strings
    private fun requestMicrophonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.RECORD_AUDIO)) {
            AlertDialog.Builder(requireContext())
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed to process pitch.")
                    .setPositiveButton("ok") { dialog: DialogInterface?, which: Int -> ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.RECORD_AUDIO), MICROPHONE_PERMISSION_CODE) }
                    .setNegativeButton("cancel") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                    .create().show()
        }
        else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), MICROPHONE_PERMISSION_CODE)
        }
    }

}