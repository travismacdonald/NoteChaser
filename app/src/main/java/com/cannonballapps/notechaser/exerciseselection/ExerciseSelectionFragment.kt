package com.cannonballapps.notechaser.exerciseselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.cannonballapps.notechaser.common.PermissionsManager
import com.cannonballapps.notechaser.ui.core.DevicePreviews
import com.cannonballapps.notechaser.ui.theme.NoteChaserTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExerciseSelectionFragment : Fragment() {

    @Inject lateinit var permissionsManager: PermissionsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                NoteChaserTheme {
                    ExerciseSelectionScreen(
                        onStartButtonClick = {
                            if (hasMicrophoneRuntimePermission()) {
                                navToExerciseListFragment(this)
                            } else {
                                requestMicrophonePermission()
                            }
                        },
                    )
                }
            }
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

@Composable
fun ExerciseSelectionScreen(
    onStartButtonClick: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.wrapContentSize()) {
            Button(
                onClick = { onStartButtonClick() },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = "Start")
            }
        }
    }
}

@DevicePreviews
@Composable
fun ExerciseSelectionScreenPreview() {
    NoteChaserTheme {
        ExerciseSelectionScreen(
            onStartButtonClick = { /* No-op */ },
        )
    }
}
