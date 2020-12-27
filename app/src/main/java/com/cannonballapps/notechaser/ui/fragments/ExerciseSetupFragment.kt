package com.cannonballapps.notechaser.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

import androidx.navigation.fragment.findNavController
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.data.NotePoolType
import com.cannonballapps.notechaser.data.exercisesetup.ExerciseSetupItem
import com.cannonballapps.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.cannonballapps.notechaser.databinding.FragmentExerciseSetupBinding
import com.cannonballapps.notechaser.playablegenerator.ParentScale
import com.cannonballapps.notechaser.playablegenerator.SingleNoteGenerator
import com.cannonballapps.notechaser.utilities.MusicTheoryUtils
import com.cannonballapps.notechaser.viewmodels.ExerciseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import kotlinx.android.synthetic.main.item_settings_slider.*

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

        subscribeToLiveData()

        makeSettingsList()
        // TODO: use when statement, different functions for creating list


        return binding.root
    }


    private fun subscribeToLiveData() {
        viewModel.notePoolType.observe(requireActivity()) { type: NotePoolType ->
            binding.notePoolTypeSingleList.summary.text = type.toString()
        }

        viewModel.chromaticDegrees.observe(requireActivity()) { degrees ->
            val activeDegrees = arrayListOf<String>()
            for (i in degrees.indices) {
                if (degrees[i]) {
                    activeDegrees.add(MusicTheoryUtils.CHROMATIC_INTERVAL_NAMES_SINGLE[i])
                }
            }
            binding.chromaticDegreesMultiList.summary.text =
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

        viewModel.settings.numQuestions.observe(viewLifecycleOwner) { value ->
            if (value != slider.value.toInt()) {
                binding.numQuestionsSlider.slider.value = value.toFloat()
                slider.valueFrom = resources.getInteger(R.integer.numQuestions_min).toFloat()
                slider.valueTo = resources.getInteger(R.integer.numQuestions_max).toFloat()
                slider.stepSize = resources.getInteger(R.integer.numQuestions_stepSize).toFloat()
            }
        }
    }

    private fun makeSettingsList() {
        // TODO: these can probably go
        viewModel.generator = SingleNoteGenerator()
        val generator = viewModel.generator as SingleNoteGenerator

        bindQuestionsHeader()
        bindNotePoolTypeChoiceSingleList()
        bindChromaticDegreeMultiList()

//        binding.diatonicMulti.obj = makeDiatonicMulti(generator.diatonicDegrees, generator.noteType)
//        binding.questionKeySingle.obj = makeQuestionKeySingle(generator.questionKey)
//        binding.parentScaleSingle.obj = makeParentScaleSingle(generator.noteType, generator.parentScale)
//        binding.modeSingle.obj = makeModeSingle(generator.mode, generator.noteType)
//        binding.playableRangeBar.obj = makePlayableRangeBar(generator.lowerBound, generator.upperBound, generator.minRange)
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
        // TODO: font color
        val notePoolTypeEntries: Array<String> =
                NotePoolType.values().map { it.toString() }.toTypedArray()

        binding.notePoolTypeSingleList.apply {
            title.text = getString(R.string.notePoolType_title)
            layout.setOnClickListener {
                var selectedIx: Int = viewModel.notePoolType.value!!.ordinal
                MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.notePoolType_title))
                        .setNegativeButton(getString(R.string.dismiss)) { _, _ ->
                            // Do nothing
                        }
                        .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                            viewModel.saveNotePoolType(NotePoolType.values()[selectedIx])
                        }
                        .setSingleChoiceItems(notePoolTypeEntries, selectedIx) { _, which ->
                            selectedIx = which
                        }
                        .show()
            }
        }
    }

    private fun bindChromaticDegreeMultiList() {
        binding.chromaticDegreesMultiList.apply {
            title.text = getString(R.string.chromaticDegrees_title)
//            summary.text = "Intervals to choose from"
            layout.setOnClickListener {
                val selectedIxs = viewModel.chromaticDegrees.value!!.clone()
                MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Chromatic Tones")
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
        }

//                isVisible = Transformations.map(noteType) { value ->
//                    value == NotePoolType.CHROMATIC
//                }

    }

    private fun makeDiatonicMulti(
            diatonicDegrees: MutableLiveData<BooleanArray>,
            noteType: MutableLiveData<NotePoolType>
    ): ExerciseSetupItem.MultiList {
        return ExerciseSetupItem.MultiList(
                "Diatonic Intervals",
                // TODO: write a better summary
                "Intervals to choose from",
                MusicTheoryUtils.DIATONIC_INTERVAL_NAMES_SINGLE,
                diatonicDegrees,
                clickListener = View.OnClickListener {
                    val tempList = diatonicDegrees.value!!.clone()
                    MaterialAlertDialogBuilder(requireContext())
                            .setTitle("DiatonicTones")
                            .setNegativeButton("Dismiss") { _, _ ->
                                // Do nothing
                            }
                            .setPositiveButton("Confirm") { _, _ ->
                                // Commit Changes
                                diatonicDegrees.value = tempList.copyOf()
                            }
                            .setMultiChoiceItems(MusicTheoryUtils.DIATONIC_INTERVAL_NAMES_SINGLE, tempList) { _, which, checked ->
                                tempList[which] = checked
                            }
                            .show()
                },
                isVisible = Transformations.map(noteType) { value ->
                    value == NotePoolType.DIATONIC
                }
        )
    }

    private fun makeQuestionKeySingle(questionKey: MutableLiveData<Int>): ExerciseSetupItem.SingleList {
        val questionKeyTitle = resources.getString(R.string.questionKey_title)
        return ExerciseSetupItem.SingleList(
                questionKeyTitle,
                MusicTheoryUtils.CHROMATIC_SCALE_FLAT,
                questionKey,
                clickListener = View.OnClickListener {
                    var tempItem = questionKey.value!!
                    MaterialAlertDialogBuilder(requireContext())
                            .setTitle(questionKeyTitle)
                            .setNegativeButton("Dismiss") { _, _ ->
                                // Do nothing
                            }
                            .setPositiveButton("Confirm") { _, _ ->
                                // Commit Changes
                                questionKey.value = tempItem
                            }
                            .setSingleChoiceItems(MusicTheoryUtils.CHROMATIC_SCALE_FLAT, tempItem) { _, which ->
                                tempItem = which
                            }
                            .show()
                }
        )
    }

    private fun makeParentScaleSingle(
            noteType: MutableLiveData<NotePoolType>,
            parentScale: MutableLiveData<ParentScale>
    ): ExerciseSetupItem.SingleList {
        val parentScaleTitle = resources.getString(R.string.parentScale_title)
        val parentScaleEntries = (MusicTheoryUtils.PARENT_SCALE_BANK.map { it.name }).toTypedArray()
        val parentScaleValue = Transformations.map(parentScale) { value ->
            parentScaleEntries.indexOf(value.name)
        }
        return ExerciseSetupItem.SingleList(
                parentScaleTitle,
                parentScaleEntries,
                parentScaleValue,
                isVisible = Transformations.map(noteType) { value ->
                    value == NotePoolType.DIATONIC
                },
                clickListener = View.OnClickListener {
                    var tempItem = parentScaleValue.value!!
                    MaterialAlertDialogBuilder(requireContext())
                            .setTitle(parentScaleTitle)
                            .setNegativeButton("Dismiss") { _, _ ->
                                // Do nothing
                            }
                            .setPositiveButton("Confirm") { _, _ ->
                                // Commit Changes
                                parentScale.value = MusicTheoryUtils.PARENT_SCALE_BANK[tempItem]
                            }
                            .setSingleChoiceItems(parentScaleEntries, tempItem) { _, which ->
                                tempItem = which
                            }
                            .show()
                }
        )
    }

    private fun makeModeSingle(
            mode: MutableLiveData<Int>,
            noteType: MutableLiveData<NotePoolType>
    ): ExerciseSetupItem.SingleList {
        // TODO: extract hard coded
        val modeTitle = "Mode"
        // TODO: turn mode and parent scale into one parameter
        val modeEntries = arrayOf("1 (Root)", "2", "3", "4", "5", "6", "7")
        val modeValue = mode
        return ExerciseSetupItem.SingleList(
                modeTitle,
                modeEntries,
                modeValue,
                isVisible = Transformations.map(noteType) { value ->
                    value == NotePoolType.DIATONIC
                },
                clickListener = View.OnClickListener {
                    var tempItem = modeValue.value!!
                    MaterialAlertDialogBuilder(requireContext())
                            .setTitle(modeTitle)
                            .setNegativeButton("Dismiss") { _, _ ->
                                // Do nothing
                            }
                            .setPositiveButton("Confirm") { _, _ ->
                                // Commit Changes
                                mode.value = tempItem
                            }
                            .setSingleChoiceItems(modeEntries, tempItem) { _, which ->
                                tempItem = which
                            }
                            .show()
                }
        )
    }

    private fun makePlayableRangeBar(
            lowerBound: MutableLiveData<Int>,
            upperBound: MutableLiveData<Int>,
            minRange: MutableLiveData<Int>
    ): ExerciseSetupItem.RangeBar {
        // TODO: make the only selectable values notes that are actually in the scale
        return ExerciseSetupItem.RangeBar(
                "Question Range",
                24f,
                88f,
                lowerBound,
                upperBound,
                displayValue = {
                    val result = MediatorLiveData<String>()
                    val updateRange = {
                        val lower = MusicTheoryUtils.ixToName(lowerBound.value!!)
                        val upper = MusicTheoryUtils.ixToName(upperBound.value!!)
                        result.value = "$lower to $upper"
                    }
                    result.addSource(lowerBound) { updateRange() }
                    result.addSource(upperBound) { updateRange() }
                    result
                }.invoke(),
                isValid = {
                    val result = MediatorLiveData<Boolean>()
                    val isValid = {
                        val lower = lowerBound.value!!
                        val upper = upperBound.value!!
                        val minRange = minRange.value!!
                        // Both upper and lower bounds are inclusive
                        result.value = upper - lower >= minRange
                    }
                    result.addSource(lowerBound) { isValid() }
                    result.addSource(upperBound) { isValid() }
                    result.addSource(minRange) { isValid() }
                    result
                }.invoke()
        )
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
        // This actually only fires once (initial observation).
        // There is a bug that I couldn't fix otherwise.

        binding.numQuestionsSlider.apply {
            this.title.text = getString(R.string.numQuestions_title)

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
                    } else if (noteType.value!! == NotePoolType.DIATONIC && !diatonicDegrees.value!!.contains(true)) {
                        Toast.makeText(context, "Must select at least one diatonic degree", Toast.LENGTH_SHORT).show()
                    } else if (noteType.value!! == NotePoolType.CHROMATIC && !chromaticDegrees.value!!.contains(true)) {
                        Toast.makeText(context, "Must select at least one chromatic degree", Toast.LENGTH_SHORT).show()
                    } else {
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

}
