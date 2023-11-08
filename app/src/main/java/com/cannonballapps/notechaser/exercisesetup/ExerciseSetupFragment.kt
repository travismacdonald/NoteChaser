package com.cannonballapps.notechaser.exercisesetup

import android.os.Bundle
import android.util.Range
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.common.ExerciseSettings
import com.cannonballapps.notechaser.common.NotePoolType
import com.cannonballapps.notechaser.common.SessionLengthSettings
import com.cannonballapps.notechaser.databinding.FragmentExerciseSetupBinding
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.ParentScale
import com.cannonballapps.notechaser.ui.theme.NoteChaserTheme
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/*
 * TODO
 *  - convert entire fragment to use compose
 */
@AndroidEntryPoint
class ExerciseSetupFragment : Fragment() {

    private val viewModel: ExerciseSetupViewModel by viewModels()
    private lateinit var binding: FragmentExerciseSetupBinding
    private lateinit var args: ExerciseSetupFragmentArgs

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

    private fun bindExerciseSetupItemsList() {
        bindNotePoolTypeChoiceSingleList()
        bindDiatonicDegreesMultiList()
        bindChromaticDegreeMultiList()
        bindScaleSingleList()

        bindNavigationButtons()

        bindUi()
    }

    private fun bindUi() {
        viewModel.exerciseSettingsStream
            .onEach(::updateUi)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isValidConfiguration
            .onEach(::updateNavButtons)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun updateUi(exerciseSettings: ExerciseSettings) {
        /*
         * Chromatic degrees
         */
        binding.chromaticDegreesMultiList.layout.isVisible = exerciseSettings.notePoolType is NotePoolType.Chromatic
        if (exerciseSettings.notePoolType is NotePoolType.Chromatic) {
            val activeChromaticDegrees = arrayListOf<String>()
            for (i in exerciseSettings.notePoolType.degrees.indices) {
                if (exerciseSettings.notePoolType.degrees[i]) {
                    activeChromaticDegrees.add(MusicTheoryUtils.CHROMATIC_INTERVAL_NAMES_SINGLE[i])
                }
            }
            binding.chromaticDegreesMultiList.summary.text = when (activeChromaticDegrees.size) {
                0 -> {
                    resources.getString(R.string.noneSelected)
                }
                exerciseSettings.notePoolType.degrees.size -> {
                    resources.getString(R.string.allSelected)
                }
                else -> {
                    activeChromaticDegrees.joinToString(separator = ", ")
                }
            }
        }

        /**
         * Diatonic degrees
         */
        binding.diatonicDegreesMultiList.layout.isVisible = exerciseSettings.notePoolType is NotePoolType.Diatonic
        if (exerciseSettings.notePoolType is NotePoolType.Diatonic) {
            val activeDiatonicDegrees = arrayListOf<String>()
            for (i in exerciseSettings.notePoolType.degrees.indices) {
                if (exerciseSettings.notePoolType.degrees[i]) {
                    activeDiatonicDegrees.add(MusicTheoryUtils.DIATONIC_INTERVAL_NAMES_SINGLE[i])
                }
            }
            binding.diatonicDegreesMultiList.summary.text = when (activeDiatonicDegrees.size) {
                0 -> {
                    resources.getString(R.string.noneSelected)
                }

                exerciseSettings.notePoolType.degrees.size -> {
                    resources.getString(R.string.allSelected)
                }

                else -> {
                    activeDiatonicDegrees.joinToString(separator = ", ")
                }
            }
        }

        /*
         * Scale
         */
        binding.scaleSingleList.layout.isVisible = exerciseSettings.notePoolType is NotePoolType.Diatonic
        if (exerciseSettings.notePoolType is NotePoolType.Diatonic) {
            val scale = exerciseSettings.notePoolType.scale
            // todo scale string stuff
            binding.scaleSingleList.summary.text = scale.toString()
        }

        /*
         * Session type
         */
        binding.sessionTypeSingleList.summary.text = exerciseSettings.sessionLengthSettings.toString()

    }

    private fun updateNavButtons(isValidConfiguration: Boolean) {
        binding.navigationButtons.startButton.isEnabled = isValidConfiguration
    }

    private fun bindNotePoolTypeChoiceSingleList() {

        binding.notePoolTypeSingleList.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.Default)
            setContent {
            }
            /* todo */
//            layout.setOnClickListener {
//                viewModel.exerciseSettingsFlow2.value.notePoolType.let { notePoolType ->
//                    showMaterialDialogSingleList(
//                        title = getString(R.string.notePoolType_title),
//                        entries = notePoolTypeNames,
//                        initSelectedIx = notePoolType.ordinal,
//                        onPositiveButtonClick = { selectedIx ->
//                            val selectedNotePool = NotePoolType.values()[selectedIx]
//                            viewModel.saveNotePoolType(selectedNotePool)
//                        },
//                    )
//                }
//            }
        }
    }

    private fun bindChromaticDegreeMultiList() {
        binding.chromaticDegreesMultiList.apply {
            title.text = getString(R.string.chromaticDegrees_title)

            layout.setOnClickListener {
                showMaterialDialogMultiList(
                    title = getString(R.string.chromaticDegrees_title),
                    entries = MusicTheoryUtils.CHROMATIC_INTERVAL_NAMES_SINGLE,
                    initSelectedIxs = booleanArrayOf(),
                    onPositiveButtonClick = { degrees ->
                        viewModel.setChromaticDegrees(degrees)
                    },
                )
            }
        }
    }

    private fun bindDiatonicDegreesMultiList() {
        binding.diatonicDegreesMultiList.apply {
            title.text = getString(R.string.diatonicDegrees_title)

            layout.setOnClickListener {
                showMaterialDialogMultiList(
                    title = getString(R.string.diatonicDegrees_title),
                    entries = MusicTheoryUtils.DIATONIC_INTERVAL_NAMES_SINGLE,
                    initSelectedIxs = booleanArrayOf(),
                    onPositiveButtonClick = { degrees ->
                        viewModel.setDiatonicDegrees(degrees)
                    },
                )
            }
        }
    }

    private fun bindScaleSingleList() {
        val parentScaleNames = listOf(
            ParentScale.Major,
            ParentScale.MelodicMinor,
            ParentScale.HarmonicMajor,
            ParentScale.HarmonicMinor,
        ).map {
            it.toString()
        }.toTypedArray()

        binding.scaleSingleList.apply {
            title.text = resources.getString(R.string.scale_title)

            // TODO: this could look better!
            layout.setOnClickListener {
                showMaterialDialogSingleList(
                    title = getString(R.string.parentScale_title),
                    entries = parentScaleNames,
                    initSelectedIx = 0,

                    onPositiveButtonClick = { selectedParentScaleIx ->
                        // todo
//                        val selectedParentScale = ParentScale.values()[selectedParentScaleIx]
                        val selectedParentScale = ParentScale.Major

                        showMaterialDialogSingleList(
                            title = getString(R.string.mode_title),
                            entries = selectedParentScale.scales.map { it.toString() }.toTypedArray(),
                            initSelectedIx = 0,

                            onPositiveButtonClick = { selectedModeIx ->
                                viewModel.setScale(selectedParentScale.scaleAtIndex(selectedModeIx))
                            },
                        )
                    },
                )
            }
        }
    }

    private fun bindNavigationButtons() {
        binding.navigationButtons.apply {
            startButton.text = getString(R.string.startSession_button)
            backButton.text = getString(R.string.back_button)

            startButton.setOnClickListener { button ->
                // TODO this should save the settings
                viewModel.saveExerciseSettings()
                navigateToExerciseSession(button)
            }

            backButton.setOnClickListener { button ->
                navigateBackToExerciseTypeMenu(button)
            }
        }

        // TODO: validate settings
    }

    private fun showMaterialDialogSingleList(
        title: String,
        entries: Array<String>,
        initSelectedIx: Int,
        onPositiveButtonClick: ((selectedIx: Int) -> Unit),
    ) {
        var curSelectedIx = initSelectedIx
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setNegativeButton(getString(R.string.dismiss)) { _, _ ->
                // Do nothing
            }
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                onPositiveButtonClick(curSelectedIx)
            }
            .setSingleChoiceItems(entries, initSelectedIx) { _, ix ->
                curSelectedIx = ix
            }
            .show()
    }

    private fun showMaterialDialogMultiList(
        title: String,
        entries: Array<String>,
        initSelectedIxs: BooleanArray,
        onPositiveButtonClick: ((selectedIxs: BooleanArray) -> Unit),
    ) {
        val curSelectedIxs = initSelectedIxs.clone()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setNegativeButton(getString(R.string.dismiss)) { _, _ ->
                // Do nothing
            }
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                onPositiveButtonClick(curSelectedIxs)
            }
            .setMultiChoiceItems(entries, curSelectedIxs) { _, _, _ ->
                /* No-op */
            }
            .show()
    }

    private fun navigateBackToExerciseTypeMenu(view: View) {
        val directions = ExerciseSetupFragmentDirections.actionExerciseSetupFragmentToExerciseSelectionFragment()
        view.findNavController().navigate(directions)
    }

    private fun navigateToExerciseSession(view: View) {
        val directions = ExerciseSetupFragmentDirections.actionExerciseSetupFragmentToSessionFragment(args.exerciseType)
        view.findNavController().navigate(directions)
    }
}
