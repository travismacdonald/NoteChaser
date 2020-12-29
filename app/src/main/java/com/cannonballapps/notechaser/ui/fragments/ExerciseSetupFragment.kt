package com.cannonballapps.notechaser.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.navigation.fragment.findNavController
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.data.NotePoolType
import com.cannonballapps.notechaser.data.ParentScale2
import com.cannonballapps.notechaser.data.exercisesetup.ExerciseSetupItem
import com.cannonballapps.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.cannonballapps.notechaser.databinding.FragmentExerciseSetupBinding
import com.cannonballapps.notechaser.playablegenerator.SingleNoteGenerator
import com.cannonballapps.notechaser.utilities.MusicTheoryUtils
import com.cannonballapps.notechaser.viewmodels.ExerciseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import timber.log.Timber

class ExerciseSetupFragment : Fragment() {

    private val viewModel: ExerciseViewModel by activityViewModels()

//    private lateinit var viewModel: ExerciseViewModel

    private lateinit var binding: FragmentExerciseSetupBinding

    private lateinit var args: ExerciseSetupFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_exercise_setup, container, false
        )
        binding.lifecycleOwner = this



        args = ExerciseSetupFragmentArgs.fromBundle(requireArguments())

//        viewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)

//        lifecycleScope.launch {
//            viewModel.preloadPrefsStore()
//        }

//        runBlocking {
//            viewModel.preloadPrefsStore()
//        }

//        subscribeToLiveData()

        makeSettingsList()
        // TODO: use when statement, different functions for creating list


        return binding.root
    }


    // TODO:
    // I thought calling these first might cause less UI issues, but maybe they should be moved
    // to their respective bindings
    private fun subscribeToLiveData() {

    }

    private fun makeSettingsList() {
        // TODO: these can probably go
        viewModel.generator = SingleNoteGenerator()
        val generator = viewModel.generator as SingleNoteGenerator

        bindQuestionsHeader()
        bindNotePoolTypeChoiceSingleList()
        bindDiatonicDegreesMultiList()
        bindChromaticDegreeMultiList()
        bindQuestionKeySingleList()
        bindScaleSingleList()
        bindPlayableRangeBar()
//        binding.sessionHeader.obj = makeSessionHeader()
//        binding.sessionLengthTypeSingle.obj = makeSessionLengthTypeSingle()

//        binding.numQuestionsSlider.obj = makeNumQuestionsSlider()
        bindNumQuestionsSlider()

//        binding.timerLengthSlider.obj = makeTimerLengthSlider()
//        binding.answerHeader.obj = makeAnswerHeader()
//        binding.matchOctaveSwitch.obj = makeMatchOctaveSwitch()
//        binding.nextButtons.obj = makeNextButton(
//                generator.hasValidRange(),
//                generator.noteType,
//                generator.diatonicDegrees,
//                generator.chromaticDegrees
//        )
    }

    private fun bindQuestionsHeader() {
        binding.questionsHeader.title.text = getString(R.string.questions_header)
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

            viewModel.notePoolType.observe(viewLifecycleOwner) { type: NotePoolType ->
                summary.text = type.toString()
            }
        }
    }

    private fun bindChromaticDegreeMultiList() {
        binding.chromaticDegreesMultiList.apply {
            title.text = getString(R.string.chromaticDegrees_title)
            layout.setOnClickListener {
                val selectedIxs = viewModel.chromaticDegrees.value!!.clone()
                MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.chromaticDegrees_title))
                        .setNegativeButton(getString(R.string.dismiss)) { _, _ ->
                            // Do nothing
                        }
                        .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                            viewModel.saveChromaticDegrees(selectedIxs)

                        }
                        .setMultiChoiceItems(MusicTheoryUtils.CHROMATIC_INTERVAL_NAMES_SINGLE, selectedIxs) { _, ix, isChecked ->
                            selectedIxs[ix] = isChecked
                        }
                        .show()
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
                val selectedIxs = viewModel.diatonicDegrees.value!!.clone()
                MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.diatonicDegrees_title))
                        .setNegativeButton(getString(R.string.dismiss)) { _, _ ->
                            // Do nothing
                        }
                        .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                            viewModel.saveDiatonicDegrees(selectedIxs)

                        }
                        .setMultiChoiceItems(MusicTheoryUtils.DIATONIC_INTERVAL_NAMES_SINGLE, selectedIxs) { _, ix, isChecked ->
                            selectedIxs[ix] = isChecked
                        }
                        .show()
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
                        initSelectedIx = viewModel.questionKey.value!!,
                        onPositiveButtonClick = { selectedIx ->
                            viewModel.saveQuestionKey(selectedIx)
                        }
                )
            }

            viewModel.questionKey.observe(viewLifecycleOwner) { key ->
                summary.text = MusicTheoryUtils.CHROMATIC_SCALE_FLAT[key]
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

            viewModel.scaleName.observe(viewLifecycleOwner) { scaleName ->
                summary.text = scaleName
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
                    MusicTheoryUtils.midiValueToNoteName(ix.toInt())
                }
                summary.text = range
            }

            // TODO: validate range

            rangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {}

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    if (slider.focusedThumbIndex == 0) {
                        viewModel.savePlayableLowerBound(slider.values[0].toInt())
                    }
                    else {
                        viewModel.savePlayableUpperBound(slider.values[1].toInt())
                    }
                }
            })

            viewModel.playableBounds.observe(viewLifecycleOwner) { bounds ->
                val boundsAsFloats = bounds.toList().map { it.toFloat() }

                if (rangeSlider.values != boundsAsFloats) {
                    rangeSlider.values = boundsAsFloats
                    rangeSlider.valueFrom = resources.getInteger(R.integer.playableBound_min).toFloat()
                    rangeSlider.valueTo = resources.getInteger(R.integer.playableBound_max).toFloat()
                    rangeSlider.stepSize = resources.getInteger(R.integer.playableBound_stepSize).toFloat()
                }
            }
        }
    }

    private fun makeSessionHeader(): ExerciseSetupItem.Header {
        return ExerciseSetupItem.Header(
                getString(R.string.session_header)
        )
    }

    private fun makeSessionLengthTypeSingle(): ExerciseSetupItem.SingleList {
        // TODO: extract hard coded
        val sessionLengthTitle = "Session Length"
        // TODO: turn mode and parent scale into one parameter
        val sessionLengthEntries = arrayOf("Question Limit", "Time Limit", "Unlimited")
        val sessionLengthValue = viewModel.settings.sessionLengthType
        return ExerciseSetupItem.SingleList(
                sessionLengthTitle,
                sessionLengthEntries,
                sessionLengthValue,
                clickListener = View.OnClickListener {
                    var tempItem = sessionLengthValue.value!!
                    MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Session Type")
                            .setNegativeButton("Dismiss") { _, _ ->
                                // Do nothing
                            }
                            .setPositiveButton("Confirm") { _, _ ->
                                // Commit Changes
                                sessionLengthValue.value = tempItem
                            }
                            .setSingleChoiceItems(sessionLengthEntries, tempItem) { _, which ->
                                tempItem = which
                            }
                            .show()
                }
        )
    }

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

            // TODO: visibility livedata observations
            // isVisible = Transformations.map(viewModel.settings.sessionLengthType) { value ->
//                    value == ExerciseSetupSettings.QUESTION_LIMIT
//                }

            // Only fires once: first observation
            viewModel.settings.numQuestions.observe(viewLifecycleOwner) { value ->
                if (value != slider.value.toInt()) {
                    binding.numQuestionsSlider.slider.value = value.toFloat()
                    slider.valueFrom = resources.getInteger(R.integer.numQuestions_min).toFloat()
                    slider.valueTo = resources.getInteger(R.integer.numQuestions_max).toFloat()
                    slider.stepSize = resources.getInteger(R.integer.numQuestions_stepSize).toFloat()
                }
            }

        }
    }


    private fun makeTimerLengthSlider(): ExerciseSetupItem.Slider {
        return ExerciseSetupItem.Slider(
                // TODO: Extract some of these numbers
                getString(R.string.timerLength_title),
                5f,
                60f,
                viewModel.settings.timerLength,
                Transformations.map(viewModel.settings.timerLength) { value ->
                    value.toString()
                },
                5f,
                isVisible = Transformations.map(viewModel.settings.sessionLengthType) { value ->
                    value == ExerciseSetupSettings.TIME_LIMIT
                }
        )
    }

    private fun makeAnswerHeader(): ExerciseSetupItem.Header {
        return ExerciseSetupItem.Header(getString(R.string.answer_header))
    }

    private fun makeMatchOctaveSwitch(): ExerciseSetupItem.Switch {
        return ExerciseSetupItem.Switch(
                getString(R.string.matchOctave_title),
                getString(R.string.matchOctave_summary),
                viewModel.settings.matchOctave)
    }

    private fun makeNextButton(
            hasValidRange: Boolean,
            noteType: MutableLiveData<NotePoolType>,
            diatonicDegrees: MutableLiveData<BooleanArray>,
            chromaticDegrees: MutableLiveData<BooleanArray>
    ): ExerciseSetupItem.Buttons {
        val nextButton = ExerciseSetupItem.Buttons(
                "Start",
                "Back",
                nextClickListener = {
                    if (!hasValidRange) {
                        Toast.makeText(context, "Not enough range to generate question", Toast.LENGTH_SHORT).show()
                    }
                    else if (noteType.value!! == NotePoolType.DIATONIC && !diatonicDegrees.value!!.contains(true)) {
                        Toast.makeText(context, "Must select at least one diatonic degree", Toast.LENGTH_SHORT).show()
                    }
                    else if (noteType.value!! == NotePoolType.CHROMATIC && !chromaticDegrees.value!!.contains(true)) {
                        Toast.makeText(context, "Must select at least one chromatic degree", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        viewModel.generator.setupGenerator()
                        val directions = ExerciseSetupFragmentDirections.actionExerciseSetupFragmentToSessionFragment()
                        findNavController().navigate(directions)
                    }
                },
                backClickListener = {
                    Toast.makeText(context, "back", Toast.LENGTH_SHORT).show()
                }
        )
        return nextButton
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


}
