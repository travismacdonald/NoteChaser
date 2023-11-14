package com.cannonballapps.notechaser.exerciselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class ExerciseListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ExerciseListScreen(
                    onNavigateToExerciseSetupFragment = {
                        findNavController().navigate(
                            ExerciseListFragmentDirections.actionExerciseListFragmentToExerciseSetupFragment(),
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun ExerciseListScreen(
    onNavigateToExerciseSetupFragment: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.wrapContentSize()) {
            FloatingActionButton(
                modifier = Modifier
                    .width(40.dp) // TODO
                    .height(40.dp), // TODO
                onClick = {
                    /*
                     * TODO: refactor exercise type
                     *       should probably just be an option in the setup fragment itself
                     */
                    onNavigateToExerciseSetupFragment()
                },
            ) {
                /*
                 * Nothing for now
                 */
            }
        }
    }
}
