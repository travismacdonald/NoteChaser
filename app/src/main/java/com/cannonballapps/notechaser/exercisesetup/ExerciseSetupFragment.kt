package com.cannonballapps.notechaser.exercisesetup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_exercise_setup,
            container,
            false,
        )
        binding.lifecycleOwner = this
        args = ExerciseSetupFragmentArgs.fromBundle(requireArguments())

        // TODO: use when statement, different functions for creating list
        viewModel.exerciseType = args.exerciseType

        bindExerciseSetupItemsList()

        return binding.root
    }

    private fun bindExerciseSetupItemsList() {
        bindQuestionsHeader()
        bindNotePoolTypeChoiceSingleList()
        bindDiatonicDegreesMultiList()
        bindChromaticDegreeMultiList()
        bindQuestionKeySingleList()
        bindScaleSingleList()
        bindPlayableRangeBar()

        bindSessionHeader()
        bindSessionTypeSingleList()
        bindNumQuestionsSlider()
        bindTimerLengthSlider()

        bindAnswerSettingsHeader()
        bindMatchOctaveSwitch()
        bindPlayStartingPitchSwitch()
        bindNavigationButtons()

        bindUi()
    }

    private fun bindUi() {
        viewModel.exerciseSettingsFlow
            .onEach(::updateUi)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isValidConfiguration
            .onEach(::updateNavButtons)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun updateUi(exerciseSettings: ExerciseSettings) {
        /*
         * Note pool type
         */
//        binding.noteTypePoolSingleList.summary.text = exerciseSettings.notePoolType.toString()
//        binding.noteTypePoolSingleList

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
         * Question key
         */
        binding.questionKeySingleList.summary.text = exerciseSettings.sessionSettings.questionKey.toString()

        /*
         * Match octave
         */
        if (exerciseSettings.sessionSettings.shouldMatchOctave != binding.matchOctaveSwitch.switchWidget.isChecked) {
            binding.matchOctaveSwitch.switchWidget.isChecked = exerciseSettings.sessionSettings.shouldMatchOctave
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
         * Num questions
         */
        binding.numQuestionsSlider.layout.isVisible = exerciseSettings.sessionLengthSettings is SessionLengthSettings.QuestionLimit
        if (exerciseSettings.sessionLengthSettings is SessionLengthSettings.QuestionLimit) {
            binding.numQuestionsSlider.apply {
                if (exerciseSettings.sessionLengthSettings.questionLimit != slider.value.toInt()) {
                    slider.value = exerciseSettings.sessionLengthSettings.questionLimit.toFloat()
                    slider.valueFrom = resources.getInteger(R.integer.numQuestions_min).toFloat()
                    slider.valueTo = resources.getInteger(R.integer.numQuestions_max).toFloat()
                    slider.stepSize = resources.getInteger(R.integer.numQuestions_stepSize).toFloat()
                }
            }
        }

        /*
         * Time limit
         */
        binding.sessionTimeLimitSlider.layout.isVisible = exerciseSettings.sessionLengthSettings is SessionLengthSettings.TimeLimit
        if (exerciseSettings.sessionLengthSettings is SessionLengthSettings.TimeLimit)
        binding.sessionTimeLimitSlider.apply {
            if (exerciseSettings.sessionLengthSettings.timeLimitMinutes != slider.value.toInt()) {
                slider.value = exerciseSettings.sessionLengthSettings.timeLimitMinutes.toFloat()
                slider.valueFrom = resources.getInteger(R.integer.sessionTimeLimit_min).toFloat()
                slider.valueTo = resources.getInteger(R.integer.sessionTimeLimit_max).toFloat()
                slider.stepSize = resources.getInteger(R.integer.sessionTimeLimit_stepSize).toFloat()
            }
        }

        /*
         * Session type
         */
        binding.sessionTypeSingleList.summary.text = exerciseSettings.sessionLengthSettings.toString()

        /*
         * Play starting pitch
         */
        if (exerciseSettings.sessionSettings.shouldPlayStartingPitch != binding.startingPitchSwitch.switchWidget.isChecked) {
            binding.startingPitchSwitch.switchWidget.isChecked = exerciseSettings.sessionSettings.shouldPlayStartingPitch
        }

        /*
         * Playable bounds
         */

        val boundsAsFloats = listOf(
            exerciseSettings.sessionSettings.playableLowerBound.midiNumber,
            exerciseSettings.sessionSettings.playableUpperBound.midiNumber,
        ).map { it.toFloat() }

        binding.playableRangeBar.apply {
            if (rangeSlider.values != boundsAsFloats) {
                rangeSlider.values = boundsAsFloats
                rangeSlider.valueFrom = resources.getInteger(R.integer.playableBound_min).toFloat()
                rangeSlider.valueTo = resources.getInteger(R.integer.playableBound_max).toFloat()
                rangeSlider.stepSize = resources.getInteger(R.integer.playableBound_stepSize).toFloat()
            }
        }
    }

    private fun updateNavButtons(isValidConfiguration: Boolean) {
        binding.navigationButtons.startButton.isEnabled = isValidConfiguration
    }

    private fun bindQuestionsHeader() {
        binding.questionsHeader.title.text = getString(R.string.questionSettings_header)
    }

    private fun bindNotePoolTypeChoiceSingleList() {
        val notePoolTypeNames = viewModel.notePoolTypes.map { it.toString() }.toTypedArray()

        binding.notePoolTypeSingleList.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.Default)
            setContent {
                NoteChaserTheme {
                    ExerciseSetupCard(
                        headerText = "Note Pool Type", // todo string resource
                        descriptionText = "Diatonic", // todo
                        onCardClick = { /* todo */ },
                    )
                }
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

    private fun bindQuestionKeySingleList() {
        binding.questionKeySingleList.apply {
            title.text = getString(R.string.questionKey_title)

            layout.setOnClickListener {
                showMaterialDialogSingleList(
                    title = getString(R.string.questionKey_title),
                    entries = MusicTheoryUtils.CHROMATIC_SCALE_FLAT,
                    initSelectedIx = 0,
                    onPositiveButtonClick = { selectedIx ->
                        val selectedPitchClass = MusicTheoryUtils.CHROMATIC_PITCH_CLASSES_FLAT[selectedIx]
                        viewModel.setQuestionKey(selectedPitchClass)
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

    private fun bindPlayableRangeBar() {
        // TODO: make the only selectable values notes that are actually in the scale
        binding.playableRangeBar.apply {
            title.text = getString(R.string.questionRange_title)

            rangeSlider.addOnChangeListener { slider, _, _ ->
                val range = slider.values.joinToString(separator = " - ") { ix ->
                    MusicTheoryUtils.midiNumberToNoteName(ix.toInt())
                }
                summary.text = range
            }

            rangeSlider.addOnSliderTouchListener(
                object : RangeSlider.OnSliderTouchListener {
                    override fun onStartTrackingTouch(slider: RangeSlider) {}

                    override fun onStopTrackingTouch(slider: RangeSlider) {
                        if (slider.focusedThumbIndex == 0) {
                            val lower = Note(slider.values[0].toInt())
                            viewModel.setPlayableLowerBound(lower)
                        } else {
                            val upper = Note(slider.values[1].toInt())
                            viewModel.setPlayableUpperBound(upper)
                        }
                    }
                },
            )
        }
    }

    private fun bindSessionHeader() {
        binding.sessionHeader.title.text = getString(R.string.sessionSettings_header)
    }

    private fun bindSessionTypeSingleList() {
        val sessionTypeNames = viewModel.sessionLengthTypes.map { it.toString() }.toTypedArray()

        binding.sessionTypeSingleList.apply {
            title.text = getString(R.string.sessionType_title)

            layout.setOnClickListener {
                showMaterialDialogSingleList(
                    title = getString(R.string.sessionType_title),
                    entries = sessionTypeNames,
                    initSelectedIx = 0,
                    onPositiveButtonClick = { selectedIx ->
                        viewModel.setSessionLengthType(
                            viewModel.sessionLengthTypes[selectedIx]
                        )
                    },
                )
            }
        }
    }

    // TODO: make items XML code consistent (eg tools:text, )
    private fun bindNumQuestionsSlider() {
        binding.numQuestionsSlider.apply {
            title.text = getString(R.string.numQuestions_title)

            slider.addOnChangeListener { _, value, _ ->
                summary.text = value.toInt().toString()
            }

            slider.addOnSliderTouchListener(
                object : Slider.OnSliderTouchListener {
                    override fun onStartTrackingTouch(slider: Slider) {}

                    override fun onStopTrackingTouch(slider: Slider) {
                        viewModel.setNumQuestions(slider.value.toInt())
                    }
                },
            )
        }
    }

    private fun bindTimerLengthSlider() {
        binding.sessionTimeLimitSlider.apply {
            title.text = getString(R.string.sessionTimeLimit_title)

            slider.addOnChangeListener { _, value, _ ->
                summary.text = getString(R.string.sessionTimeLimit_summary, value.toInt())
            }

            slider.addOnSliderTouchListener(
                object : Slider.OnSliderTouchListener {
                    override fun onStartTrackingTouch(slider: Slider) {}

                    override fun onStopTrackingTouch(slider: Slider) {
                        viewModel.setTimeLimitMinutes(slider.value.toInt())
                    }
                },
            )
        }
    }

    private fun bindAnswerSettingsHeader() {
        binding.answerSettingsHeader.title.text = getString(R.string.answerSettings_header)
    }

    private fun bindMatchOctaveSwitch() {
        binding.matchOctaveSwitch.apply {
            title.text = getString(R.string.matchOctave_title)
            summary.text = getString(R.string.matchOctave_summary)
            switchWidget.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setMatchOctave(isChecked)
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

    private fun bindPlayStartingPitchSwitch() {
        binding.startingPitchSwitch.apply {
            title.text = getString(R.string.playStartingPitch_title)
            summary.text = getString(R.string.playStartingPitch_summary)
            switchWidget.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setShouldPlayStartingPitch(isChecked)
            }
        }
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
