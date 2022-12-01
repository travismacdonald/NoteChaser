package com.cannonballapps.notechaser.exerciselist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDirections
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import com.cannonballapps.notechaser.common.ExerciseType
import timber.log.Timber

/**
 * Separate PRs
 *     - list items component item & preview
 *     - create button item & preview
 *     - wiring view model
 */
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
                    onNavigateToExerciseSetupFragment = { directions -> findNavController().navigate(directions) }
                )
            }
        }
    }
}

@Composable
fun ExerciseListScreen(
    onNavigateToExerciseSetupFragment: (NavDirections) -> Unit,
    viewModel: ExerciseListViewModel = viewModel(),
) {
    val navController = rememberNavController()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                modifier = Modifier
                    .width(40.dp) // TODO
                    .height(40.dp), // TODO
                onClick = {
                    val directions = ExerciseListFragmentDirections.actionExerciseListFragmentToExerciseSetupFragment(ExerciseType.SINGLE_NOTE)
                    onNavigateToExerciseSetupFragment(directions)
//                    navController.navigate(directions)
                }
            ) {

            }
        }
    }
}
