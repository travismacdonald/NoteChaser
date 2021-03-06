package com.cannonballapps.notechaser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.musicutilities.NotePoolType
import com.cannonballapps.notechaser.data.SessionType
import com.cannonballapps.notechaser.databinding.FragmentExerciseSetupBinding
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.NoteFactory
import com.cannonballapps.notechaser.musicutilities.ParentScale2
import com.cannonballapps.notechaser.viewmodels.ExerciseSetupViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ExerciseSetupFragment : Fragment() {

    private val viewModel: ExerciseSetupViewModel by viewModels()
    private lateinit var binding: FragmentExerciseSetupBinding
    private lateinit var args: ExerciseSetupFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        Timber.d("backStack count: ${requireActivity().supportFragmentManager.backStackEntryCount}")

        viewModel.prefetchPrefsStore()

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_exercise_setup, container, false
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
    }

    private fun bindQuestionsHeader() {
        binding.questionsHeader.title.text = getString(R.string.questionSettings_header)
    }

    private fun bindNotePoolTypeChoiceSingleList() {
        val notePoolTypeNames = NotePoolType.values().map { it.toString() }.toTypedArray()

        binding.notePoolTypeSingleList.apply {
            title.text = getString(R.string.notePoolType_title)
            image.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_music_note_black_40dp, requireContext().theme))

            layout.setOnClickListener {
                showMaterialDialogSingleList(
                        title = getString(R.string.notePoolType_title),
                        entries = notePoolTypeNames,
                        initSelectedIx = viewModel.notePoolType.value!!.ordinal,
                        onPositiveButtonClick = { selectedIx ->
                            val selectedNotePool = NotePoolType.values()[selectedIx]
                            viewModel.saveNotePoolType(selectedNotePool)
                        }
                )
            }

            viewModel.notePoolType.observe(viewLifecycleOwner) { notePoolType ->
                summary.text = notePoolType.toString()
            }
        }
    }

    private fun bindChromaticDegreeMultiList() {
        binding.chromaticDegreesMultiList.apply {
            title.text = getString(R.string.chromaticDegrees_title)

            layout.setOnClickListener {
                showMaterialDialogMultiList(
                        title = getString(R.string.chromaticDegrees_title),
                        entries = MusicTheoryUtils.CHROMATIC_INTERVAL_NAMES_SINGLE,
                        initSelectedIxs = viewModel.chromaticDegrees.value!!,
                        onPositiveButtonClick = { degrees ->
                            viewModel.saveChromaticDegrees(degrees)
                        }
                )
            }

            viewModel.notePoolType.observe(viewLifecycleOwner) { type ->
                layout.isVisible = type == NotePoolType.CHROMATIC
            }

            viewModel.chromaticDegrees.observe(viewLifecycleOwner) { degrees ->
                val activeDegrees = arrayListOf<String>()
                for (i in degrees.indices) {
                    if (degrees[i]) {
                        activeDegrees.add(MusicTheoryUtils.CHROMATIC_INTERVAL_NAMES_SINGLE[i])
                    }
                }
                binding.chromaticDegreesMultiList.summary.text =
                        when (activeDegrees.size) {
                            0 -> {
                                resources.getString(R.string.noneSelected)
                            }
                            degrees.size -> {
                                resources.getString(R.string.allSelected)
                            }
                            else -> {
                                activeDegrees.joinToString(separator = ", ")
                            }
                        }
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
                        initSelectedIxs = viewModel.diatonicDegrees.value!!,
                        onPositiveButtonClick = { degrees ->
                            viewModel.saveDiatonicDegrees(degrees)
                        }
                )
            }

            viewModel.notePoolType.observe(viewLifecycleOwner) { type ->
                layout.isVisible = type == NotePoolType.DIATONIC
            }

            viewModel.diatonicDegrees.observe(viewLifecycleOwner) { degrees ->
                val activeDegrees = arrayListOf<String>()
                for (i in degrees.indices) {
                    if (degrees[i]) {
                        activeDegrees.add(MusicTheoryUtils.DIATONIC_INTERVAL_NAMES_SINGLE[i])
                    }
                }
                binding.diatonicDegreesMultiList.summary.text =
                        when (activeDegrees.size) {
                            0 -> {
                                getString(R.string.noneSelected)
                            }
                            degrees.size -> {
                                getString(R.string.allSelected)
                            }
                            else -> {
                                activeDegrees.joinToString(separator = ", ")
                            }
                        }
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
                        initSelectedIx = viewModel.questionKey.value!!.value,
                        onPositiveButtonClick = { selectedIx ->
                            val selectedPitchClass = MusicTheoryUtils.CHROMATIC_PITCH_CLASSES_FLAT[selectedIx]
                            viewModel.saveQuestionKey(selectedPitchClass)
                        }
                )
            }

            viewModel.questionKey.observe(viewLifecycleOwner) { key ->
                summary.text = key.toString()
            }
        }
    }

    private fun bindScaleSingleList() {
        val parentScaleNames = ParentScale2.values().map { it.toString() }.toTypedArray()

        binding.scaleSingleList.apply {
            title.text = resources.getString(R.string.scale_title)

            // TODO: this could look better!
            layout.setOnClickListener {

                showMaterialDialogSingleList(
                        title = getString(R.string.parentScale_title),
                        entries = parentScaleNames,
                        initSelectedIx = viewModel.parentScale.value!!.ordinal,

                        onPositiveButtonClick = { selectedParentScaleIx ->

                            val selectedParentScale = ParentScale2.values()[selectedParentScaleIx]

                            showMaterialDialogSingleList(
                                    title = getString(R.string.mode_title),
                                    entries = selectedParentScale.modeNames.toTypedArray(),
                                    initSelectedIx = 0,

                                    onPositiveButtonClick = { selectedModeIx ->
                                        viewModel.saveParentScale(selectedParentScale)
                                        viewModel.saveModeIx(selectedModeIx)
                                    }
                            )
                        }
                )
            }

            viewModel.scale.observe(viewLifecycleOwner) { scale ->
                summary.text = scale.name
            }

            viewModel.notePoolType.observe(viewLifecycleOwner) { type ->
                layout.isVisible = type == NotePoolType.DIATONIC
            }
        }
    }

    private fun bindPlayableRangeBar() {
        // TODO: make the only selectable values notes that are actually in the scale
        binding.playableRangeBar.apply {
            title.text = getString(R.string.questionRange_title)

            rangeSlider.addOnChangeListener { slider, value, _ ->
                val range = slider.values.joinToString(separator = " - ") { ix ->
                    MusicTheoryUtils.midiNumberToNoteName(ix.toInt())
                }
                summary.text = range
            }

            rangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {}

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    if (slider.focusedThumbIndex == 0) {
                        val lower = NoteFactory.makeNoteFromMidiNumber(slider.values[0].toInt())
                        viewModel.savePlayableLowerBound(lower)
                    }
                    else {
                        val upper = NoteFactory.makeNoteFromMidiNumber(slider.values[1].toInt())
                        viewModel.savePlayableUpperBound(upper)
                    }
                }
            })

            viewModel.playableBounds.observe(viewLifecycleOwner) { bounds ->
                val boundsAsFloats = bounds.toList().map { it.midiNumber.toFloat() }

                if (rangeSlider.values != boundsAsFloats) {
                    rangeSlider.values = boundsAsFloats
                    rangeSlider.valueFrom = resources.getInteger(R.integer.playableBound_min).toFloat()
                    rangeSlider.valueTo = resources.getInteger(R.integer.playableBound_max).toFloat()
                    rangeSlider.stepSize = resources.getInteger(R.integer.playableBound_stepSize).toFloat()
                }
            }
        }
    }

    private fun bindSessionHeader() {
        binding.sessionHeader.title.text = getString(R.string.sessionSettings_header)
    }

    private fun bindSessionTypeSingleList() {
        val sessionTypeNames = SessionType.values().map { it.toString() }.toTypedArray()

        binding.sessionTypeSingleList.apply {
            title.text = getString(R.string.sessionType_title)

            layout.setOnClickListener {
                showMaterialDialogSingleList(
                        title = getString(R.string.sessionType_title),
                        entries = sessionTypeNames,
                        initSelectedIx = viewModel.sessionType.value!!.ordinal,
                        onPositiveButtonClick = { selectedIx ->
                            val selectedSessionType = SessionType.values()[selectedIx]
                            viewModel.saveSessionType(selectedSessionType)
                        }
                )
            }

            viewModel.sessionType.observe(viewLifecycleOwner) { sessionType ->
                summary.text = sessionType.toString()
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

            slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: Slider) {}

                override fun onStopTrackingTouch(slider: Slider) {
                    viewModel.saveNumQuestions(slider.value.toInt())
                }
            })

            viewModel.sessionType.observe(viewLifecycleOwner) { type ->
                layout.isVisible = type == SessionType.QUESTION_LIMIT
            }

            // Only fires once: first observation
            viewModel.numQuestions.observe(viewLifecycleOwner) { numQuestions ->
                if (numQuestions != slider.value.toInt()) {
                    slider.value = numQuestions.toFloat()
                    slider.valueFrom = resources.getInteger(R.integer.numQuestions_min).toFloat()
                    slider.valueTo = resources.getInteger(R.integer.numQuestions_max).toFloat()
                    slider.stepSize = resources.getInteger(R.integer.numQuestions_stepSize).toFloat()
                }
            }
        }
    }

    private fun bindTimerLengthSlider() {
        binding.sessionTimeLimitSlider.apply {
            title.text = getString(R.string.sessionTimeLimit_title)

            slider.addOnChangeListener { _, value, _ ->
                summary.text = getString(R.string.sessionTimeLimit_summary, value.toInt())
            }

            slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: Slider) {}

                override fun onStopTrackingTouch(slider: Slider) {
                    viewModel.saveSessionTimeLimit(slider.value.toInt())
                }
            })

            viewModel.sessionType.observe(viewLifecycleOwner) { type ->
                layout.isVisible = type == SessionType.TIME_LIMIT
            }

            // Only fires once: first observation
            viewModel.sessionTimeLimit.observe(viewLifecycleOwner) { value ->
                if (value != slider.value.toInt()) {
                    slider.value = value.toFloat()
                    slider.valueFrom = resources.getInteger(R.integer.sessionTimeLimit_min).toFloat()
                    slider.valueTo = resources.getInteger(R.integer.sessionTimeLimit_max).toFloat()
                    slider.stepSize = resources.getInteger(R.integer.sessionTimeLimit_stepSize).toFloat()
                }
            }
        }
    }

    private fun bindAnswerSettingsHeader() {
        binding.answerSettingsHeader.title.text = getString(R.string.answerSettings_header)
    }

    private fun bindMatchOctaveSwitch() {
        binding.matchOctaveSwitch.apply {
            title.text = getString(R.string.matchOctave_title)
            summary.text = getString(R.string.matchOctave_summary)
            viewModel.matchOctave.observe(viewLifecycleOwner) { matchOctave ->
                if (matchOctave != switchWidget.isChecked) {
                    switchWidget.isChecked = matchOctave
                }
            }
            switchWidget.setOnCheckedChangeListener { _, isChecked ->
                viewModel.saveMatchOctave(isChecked)
            }
        }
    }

    private fun bindNavigationButtons() {
        binding.navigationButtons.apply {
            startButton.text = getString(R.string.startSession_button)
            backButton.text = getString(R.string.back_button)

            startButton.setOnClickListener { button ->
                navigateToExerciseSession(button)
            }

            backButton.setOnClickListener { button ->
                navigateBackToExerciseTypeMenu(button)
            }

            viewModel.isValidConfiguration.observe(viewLifecycleOwner) { isValid ->
                startButton.isEnabled = isValid
            }

        }

        // TODO: validate settings
    }

    // Thanks, u/justjoshingofficial.
    private fun bindPlayStartingPitchSwitch() {
        binding.startingPitchSwitch.apply {
            title.text = getString(R.string.playStartingPitch_title)
            summary.text = getString(R.string.playStartingPitch_summary)
            viewModel.playStartingPitch.observe(viewLifecycleOwner) { playPitch ->
                if (playPitch != switchWidget.isChecked) {
                    switchWidget.isChecked = playPitch
                }
            }
            switchWidget.setOnCheckedChangeListener { _, isChecked ->
                viewModel.savePlayStartingPitch(isChecked)
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
            onPositiveButtonClick: ((selectedIxs: BooleanArray) -> Unit)
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
                .setMultiChoiceItems(entries, curSelectedIxs) { _, ix, isChecked ->
                    curSelectedIxs[ix] = isChecked
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
