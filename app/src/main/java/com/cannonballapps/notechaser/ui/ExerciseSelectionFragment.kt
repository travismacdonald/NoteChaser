package com.cannonballapps.notechaser.ui

import android.Manifest
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
import androidx.navigation.findNavController
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.data.ExerciseType
import com.cannonballapps.notechaser.databinding.FragmentExerciseSelectionBinding
import timber.log.Timber


class ExerciseSelectionFragment : Fragment() {

    private lateinit var binding: FragmentExerciseSelectionBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {


        Timber.d("backStack count: ${requireActivity().supportFragmentManager.backStackEntryCount}")

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_exercise_selection,
                container,
                false)


        if (!hasMicrophoneRuntimePermission()) {
            requestMicrophonePermission()
        }

        binding.apply {
            singleNoteButton.setOnClickListener { view ->
                if (hasMicrophoneRuntimePermission()) {
                    navToExerciseSelectionFragment(view, ExerciseType.SINGLE_NOTE)
                }
                else {
                    requestMicrophonePermission()
                }
            }
        }

        return binding.root
    }

    private fun navToExerciseSelectionFragment(view: View, type: ExerciseType) {
        val directions = ExerciseSelectionFragmentDirections.actionExerciseSelectionFragmentToExerciseSetupFragment(type)
        view.findNavController().navigate(directions)
    }

    private fun hasMicrophoneRuntimePermission(): Boolean {
        return (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestMicrophonePermission() {
        val micPermissionCode = resources.getInteger(R.integer.microphone_permission_code)
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.RECORD_AUDIO)) {
            AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.microphonePermission_title))
                    .setMessage(getString(R.string.microphonePermission_message))
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        ActivityCompat.requestPermissions(
                                requireActivity(),
                                arrayOf(Manifest.permission.RECORD_AUDIO),
                                micPermissionCode
                        )
                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create().show()
        }
        else {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    micPermissionCode)
        }
    }

}