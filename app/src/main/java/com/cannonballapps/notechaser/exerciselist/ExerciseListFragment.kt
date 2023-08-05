package com.cannonballapps.notechaser.exerciselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
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
import com.cannonballapps.notechaser.common.ExerciseType
import com.cannonballapps.notechaser.exercisegenerator.GeneratorType

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
                    onNavigateToExerciseSetupFragment = { exerciseType ->
                        findNavController().navigate(
                            ExerciseListFragmentDirections.actionExerciseListFragmentToExerciseSetupFragment(
                                exerciseType
                            ),
                        )
                    },
                    onNavigateToScaleHarmony = {
                        findNavController().navigate(
                        ExerciseListFragmentDirections.actionExerciseListFragmentToExerciseGeneratorFragment(GeneratorType.HarmScale)
                        )
                    },
                    onNavigateToStandardHarmony = {
                        findNavController().navigate(
                        ExerciseListFragmentDirections.actionExerciseListFragmentToExerciseGeneratorFragment(GeneratorType.HarmStandard)
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun ExerciseListScreen(
    onNavigateToExerciseSetupFragment: (ExerciseType) -> Unit,
    onNavigateToStandardHarmony: () -> Unit,
    onNavigateToScaleHarmony: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.wrapContentSize()) {
            Button(
                onClick = {
                    /*
                     * TODO: refactor exercise type
                     *       should probably just be an option in the setup fragment itself
                     */
                    onNavigateToExerciseSetupFragment(ExerciseType.SINGLE_NOTE)
                },
            ) {
                 Text(text = "Exercise Setup")
            }
            Button(onClick = { onNavigateToScaleHarmony() }) {
               Text(text = "Scale - Harmonic") 
            }
            Button(onClick = { onNavigateToStandardHarmony() }) {
                Text(text = "Standard - Harmonic")
            }
        }
    }
}
