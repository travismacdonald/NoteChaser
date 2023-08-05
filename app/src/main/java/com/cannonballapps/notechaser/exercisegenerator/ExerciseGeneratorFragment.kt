package com.cannonballapps.notechaser.exercisegenerator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

enum class GeneratorType { HarmStandard, HarmScale }

class ExerciseGeneratorFragment : Fragment() {

    private val viewModel: ExerciseGeneratorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val args = ExerciseGeneratorFragmentArgs.fromBundle(requireArguments())
        viewModel.setType(args.generatorType)

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ExerciseGeneratorScreen()
            }
        }
    }
}

@Composable
fun ExerciseGeneratorScreen(
    viewModel: ExerciseGeneratorViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.wrapContentSize()) {
            val data = viewModel.exerciseFlow.collectAsState()

            val exercises = (data.value as? ExerciseState.Available)?.exercise ?: return@Column

            Text(text = "Scale = ${exercises.scale.scaleAtIndex(0).name}")
            Text(text = "Cycle = ${exercises.cycle}")
            Text(text = "Voicing Type = ${exercises.voicingType}")
        }
    }
}
