package com.cannonballapps.notechaser.exercisesetup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.cannonballapps.notechaser.ui.theme.NoteChaserTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseSetupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
       return ComposeView(requireContext()).apply {
           setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
           setContent {
               NoteChaserTheme {
                   ExerciseSetupScreen()
               }
           }
       }
    }
}
