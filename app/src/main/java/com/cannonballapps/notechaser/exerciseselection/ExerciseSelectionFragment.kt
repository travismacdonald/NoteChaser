package com.cannonballapps.notechaser.exerciseselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.common.PermissionsManager
import com.cannonballapps.notechaser.databinding.FragmentExerciseSelectionBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExerciseSelectionFragment : Fragment() {

    private lateinit var binding: FragmentExerciseSelectionBinding

    @Inject lateinit var permissionsManager: PermissionsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_exercise_selection,
            container,
            false,
        )

        if (!hasMicrophoneRuntimePermission()) {
            requestMicrophonePermission()
        }

        binding.apply {
            singleNoteButton.setOnClickListener { view ->
                if (hasMicrophoneRuntimePermission()) {
                    navToExerciseListFragment(view)
                } else {
                    requestMicrophonePermission()
                }
            }
        }

        return binding.root
    }

    private fun navToExerciseListFragment(view: View) {
        val directions = ExerciseSelectionFragmentDirections.actionExerciseSelectionFragmentToExerciseListFragment()
        view.findNavController().navigate(directions)
    }

    private fun hasMicrophoneRuntimePermission() =
        permissionsManager.isRecordAudioPermissionGranted(requireContext())

    private fun requestMicrophonePermission() =
        permissionsManager.requestRecordAudioPermission(requireActivity(), requireContext())
}
