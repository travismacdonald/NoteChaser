package com.example.notechaser.ui.fragments

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
import com.example.notechaser.R
import com.example.notechaser.data.exercisesetup.ExerciseSetupItem
import com.example.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.example.notechaser.databinding.FragmentExerciseSetupBinding
import com.example.notechaser.playablegenerator.GeneratorNoteType
import com.example.notechaser.playablegenerator.ParentScale
import com.example.notechaser.playablegenerator.SingleNoteGenerator
import com.example.notechaser.ui.adapters.ExerciseSetupAdapter
import com.example.notechaser.utilities.MusicTheoryUtils
import com.example.notechaser.viewmodels.ExerciseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ExerciseSetupFragment : Fragment() {

    val viewModel: ExerciseViewModel by activityViewModels()
    lateinit var binding: FragmentExerciseSetupBinding
    lateinit var adapter: ExerciseSetupAdapter
    lateinit var args: ExerciseSetupFragmentArgs
    private val settingItemsArray: MutableList<ExerciseSetupItem> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_exercise_setup, container, false
        )
        binding.lifecycleOwner = this

//        val manager = LinearLayoutManager(activity)
//        adapter = ExerciseSetupAdapter(this)
        args = ExerciseSetupFragmentArgs.fromBundle(arguments!!)

//        // TODO: may have to make this mutable later on
//        when (args.exerciseType) {
//            ExerciseType.SINGLE_NOTE -> makeSingleNoteList()
//            ExerciseType.INTERVALLIC -> makeIntervallicList()
//            ExerciseType.HARMONIC -> makeHarmonicList()
//            ExerciseType.SCALE -> makeScaleList()
//            ExerciseType.MELODIC -> makeMelodicList()
//            else -> throw IllegalArgumentException("Unknown exercise type: ${args.exerciseType}")
//        }

//        adapter.submitList(settingItemsArray)
//        binding.settingsList.adapter = adapter
//        binding.settingsList.layoutManager = manager

        makeSettingsList();

        return binding.root
    }


    private fun makeSettingsList() {
        // todo: question type header
        binding.noteChoiceHeader.obj = makeQuestionsHeader()
    }

    private fun makeSingleNoteList() {

        viewModel.generator = SingleNoteGenerator()
        val generator = viewModel.generator as SingleNoteGenerator

        /* Questions */
        makeQuestionsHeader()
        addNoteChoiceSingle(generator.noteType)
        addChromaticMulti(generator.chromaticDegrees, generator.noteType)
        addDiatonicMulti(generator.diatonicDegrees, generator.noteType)
        addQuestionKeySingle(generator.questionKey)
        addParentScaleSingle(generator.noteType, generator.parentScale)
        addModeSingle(generator.mode, generator.noteType)
        addPlayableRangeBar(generator.lowerBound, generator.upperBound, generator.minRange)
        addSessionHeader()
        addSessionLengthSingle()
        addNumQuestionsSlider()
        addTimerLengthSlider()
        addAnswerHeader()
        addMatchOctaveSwitch()
        addNextButton(
                generator.hasValidRange(),
                generator.noteType,
                generator.diatonicDegrees,
                generator.chromaticDegrees
        )
    }


    /**
     * Header for note question related settings.
     */
    private fun makeQuestionsHeader() : ExerciseSetupItem.Header {
        return ExerciseSetupItem.Header(getString(R.string.questions_header))
    }

    /**
     * TODO: Function write up
     */
    private fun addNoteChoiceSingle(noteType: MutableLiveData<GeneratorNoteType>) {
        val noteChoiceTitle = resources.getString(R.string.noteChoice_title)
        val noteChoiceArray = resources.getStringArray(R.array.notechoice_entries)
        val noteChoiceValue = Transformations.map(noteType) { value ->
            val res: Int = when (value) {
                // TODO: perhaps not the most efficient, but quite readable
                GeneratorNoteType.DIATONIC -> noteChoiceArray.indexOf("Diatonic")
                GeneratorNoteType.CHROMATIC -> noteChoiceArray.indexOf("Chromatic")
                else -> -1
            }
            res
        }
        val noteChoiceSingle: ExerciseSetupItem =
                ExerciseSetupItem.SingleList(
                        noteChoiceTitle,
                        noteChoiceArray,
                        noteChoiceValue,
                        clickListener = View.OnClickListener {
                            var tempItem = noteChoiceValue.value!!
                            MaterialAlertDialogBuilder(context!!)
                                    .setTitle(noteChoiceTitle)
                                    .setNegativeButton("Dismiss") { _, _ ->
                                        // Do nothing
                                    }
                                    .setPositiveButton("Confirm") { _, _ ->
                                        // Commit Changes
                                        when (tempItem) {
                                            // TODO: perhaps not the most efficient, but quite readable
                                            noteChoiceArray.indexOf("Diatonic") -> {
                                                noteType.value = GeneratorNoteType.DIATONIC
                                            }
                                            noteChoiceArray.indexOf("Chromatic") -> {
                                                noteType.value = GeneratorNoteType.CHROMATIC
                                            }
                                            else -> -1
                                        }
                                    }
                                    .setSingleChoiceItems(noteChoiceArray, tempItem) { _, which ->
                                        tempItem = which
                                    }
                                    .show()

                        }
                )
        settingItemsArray.add(noteChoiceSingle)
    }

    /**
     * TODO: Function write up
     */
    private fun addChromaticMulti(
            chromaticDegrees: MutableLiveData<BooleanArray>,
            noteType: MutableLiveData<GeneratorNoteType>
    ) {
        val chromaticMulti: ExerciseSetupItem =
                ExerciseSetupItem.MultiList(
                        "Chromatic Intervals",
                        // TODO: write a better summary and extract
                        "Intervals to choose from",
                        MusicTheoryUtils.CHROMATIC_INTERVAL_NAMES_SINGLE,
                        chromaticDegrees,
                        clickListener = View.OnClickListener {
                            val tempList = chromaticDegrees.value!!.clone()
                            MaterialAlertDialogBuilder(context!!)
                                    .setTitle("Chromatic Tones")
                                    .setNegativeButton("Dismiss") { _, _ ->
                                        // Do nothing
                                    }
                                    .setPositiveButton("Confirm") { _, _ ->
                                        // Commit Changes
                                        chromaticDegrees.value = tempList.copyOf()

                                    }
                                    .setMultiChoiceItems(MusicTheoryUtils.CHROMATIC_INTERVAL_NAMES_SINGLE, tempList) { _, which, checked ->
                                        tempList[which] = checked
                                    }
                                    .show()
                        },
                        isVisible = Transformations.map(noteType) { value ->
                            value == GeneratorNoteType.CHROMATIC
                        }
                )
        settingItemsArray.add(chromaticMulti)
    }

    /**
     * TODO: Function write up
     */
    private fun addDiatonicMulti(
            diatonicDegrees: MutableLiveData<BooleanArray>,
            noteType: MutableLiveData<GeneratorNoteType>
    ) {
        val diatonicMulti: ExerciseSetupItem =
                ExerciseSetupItem.MultiList(
                        "Diatonic Intervals",
                        // TODO: write a better summary
                        "Intervals to choose from",
                        MusicTheoryUtils.DIATONIC_INTERVAL_NAMES_SINGLE,
                        diatonicDegrees,
                        clickListener = View.OnClickListener {
                            val tempList = diatonicDegrees.value!!.clone()
                            MaterialAlertDialogBuilder(context!!)
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
                            value == GeneratorNoteType.DIATONIC
                        }
                )
        settingItemsArray.add(diatonicMulti)
    }

    /**
     * TODO: Function write up
     */
    private fun addQuestionKeySingle(questionKey: MutableLiveData<Int>) {
        val questionKeyTitle = resources.getString(R.string.questionKey_title)
        val questionKeySingle: ExerciseSetupItem =
                ExerciseSetupItem.SingleList(
                        questionKeyTitle,
                        MusicTheoryUtils.CHROMATIC_SCALE_FLAT,
                        questionKey,
                        clickListener = View.OnClickListener {
                            var tempItem = questionKey.value!!
                            MaterialAlertDialogBuilder(context!!)
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
        settingItemsArray.add(questionKeySingle)
    }

    /**
     * TODO: Function write up
     */
    private fun addParentScaleSingle(
            noteType: MutableLiveData<GeneratorNoteType>,
            parentScale: MutableLiveData<ParentScale>
    ) {
        val parentScaleTitle = resources.getString(R.string.parentScale_title)
        val parentScaleEntries = (MusicTheoryUtils.PARENT_SCALE_BANK.map { it.name }).toTypedArray()
        val parentScaleValue = Transformations.map(parentScale) { value ->
            parentScaleEntries.indexOf(value.name)
        }
        val parentScaleSingle: ExerciseSetupItem =
                ExerciseSetupItem.SingleList(
                        parentScaleTitle,
                        parentScaleEntries,
                        parentScaleValue,
                        isVisible = Transformations.map(noteType) { value ->
                            value == GeneratorNoteType.DIATONIC
                        },
                        clickListener = View.OnClickListener {
                            var tempItem = parentScaleValue.value!!
                            MaterialAlertDialogBuilder(context!!)
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
        settingItemsArray.add(parentScaleSingle)
    }

    /**
     * TODO: Function write up
     */
    private fun addModeSingle(
            mode: MutableLiveData<Int>,
            noteType: MutableLiveData<GeneratorNoteType>
    ) {
        // TODO: extract hard coded
        val modeTitle = "Mode"
        // TODO: turn mode and parent scale into one parameter
        val modeEntries = arrayOf("1 (Root)", "2", "3", "4", "5", "6", "7")
        val modeValue = mode
        val modeSingle: ExerciseSetupItem =
                ExerciseSetupItem.SingleList(
                        modeTitle,
                        modeEntries,
                        modeValue,
                        isVisible = Transformations.map(noteType) { value ->
                            value == GeneratorNoteType.DIATONIC
                        },
                        clickListener = View.OnClickListener {
                            var tempItem = modeValue.value!!
                            MaterialAlertDialogBuilder(context!!)
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
        settingItemsArray.add(modeSingle)
    }

    /**
     * TODO: Function write up
     */
    private fun addPlayableRangeBar(
            lowerBound: MutableLiveData<Int>,
            upperBound: MutableLiveData<Int>,
            minRange: MutableLiveData<Int>
    ) {
        // TODO: make the only selectable values notes that are actually in the scale
        val playableRangeBar: ExerciseSetupItem =
                ExerciseSetupItem.RangeBar(
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
        settingItemsArray.add(playableRangeBar)
    }

    /**
     * TODO: Function write up
     */
    private fun addSessionHeader() {
        val sessionHeader: ExerciseSetupItem =
                ExerciseSetupItem.Header(
                        getString(R.string.session_header)
                )
        settingItemsArray.add(sessionHeader)
    }

    /**
     * TODO: Function write up
     */
    private fun addSessionLengthSingle() {
        // TODO: extract hard coded
        val sessionLengthTitle = "Session Length"
        // TODO: turn mode and parent scale into one parameter
        val sessionLengthEntries = arrayOf("Question Limit", "Time Limit", "Unlimited")
        val sessionLengthValue = viewModel.settings.sessionLengthType
        val sessionLengthSingle: ExerciseSetupItem =
                ExerciseSetupItem.SingleList(
                        sessionLengthTitle,
                        sessionLengthEntries,
                        sessionLengthValue,
                        clickListener = View.OnClickListener {
                            var tempItem = sessionLengthValue.value!!
                            MaterialAlertDialogBuilder(context!!)
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
        settingItemsArray.add(sessionLengthSingle)
    }

    /**
     * TODO: Function write up
     */
    private fun addNumQuestionsSlider() {
        val numQuestionsSlider: ExerciseSetupItem =
                ExerciseSetupItem.Slider(
                        // TODO: Extract some of these numbers
                        getString(R.string.numQuestions_title),
                        10f,
                        200f,
                        viewModel.settings.numQuestions,
                        Transformations.map(viewModel.settings.numQuestions) { value ->
                            value.toString()
                        },
                        10f,
                        isVisible = Transformations.map(viewModel.settings.sessionLengthType) { value ->
                            value == ExerciseSetupSettings.QUESTION_LIMIT
                        }

                )
        settingItemsArray.add(numQuestionsSlider)
    }

    /**
     * TODO: Function write up
     */
    private fun addTimerLengthSlider() {
        val timerLengthSlider: ExerciseSetupItem =
                ExerciseSetupItem.Slider(
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
        settingItemsArray.add(timerLengthSlider)
    }

    /**
     * TODO: Function write up
     */
    private fun addAnswerHeader() {
        val answerHeader: ExerciseSetupItem =
                ExerciseSetupItem.Header(
                        getString(R.string.answer_header)
                )
        settingItemsArray.add(answerHeader)
    }

    /**
     * TODO: Function write up
     */
    private fun addMatchOctaveSwitch() {
        val matchOctaveSwitch: ExerciseSetupItem =
                ExerciseSetupItem.Switch(
                        getString(R.string.matchOctave_title),
                        getString(R.string.matchOctave_summary),
                        viewModel.settings.matchOctave)
        settingItemsArray.add(matchOctaveSwitch)
    }

    /**
     * TODO: Function write up
     */
    private fun addNextButton(
            hasValidRange: Boolean,
            noteType: MutableLiveData<GeneratorNoteType>,
            diatonicDegrees: MutableLiveData<BooleanArray>,
            chromaticDegrees: MutableLiveData<BooleanArray>
    ) {
        val nextButton: ExerciseSetupItem =
                ExerciseSetupItem.Buttons(
                        "Start",
                        "Back",
                        nextClickListener = View.OnClickListener {
                            if (!hasValidRange) {
                                Toast.makeText(context, "Not enough range to generate question", Toast.LENGTH_SHORT).show()
                            }
                            else if (noteType.value!! == GeneratorNoteType.DIATONIC && !diatonicDegrees.value!!.contains(true)) {
                                Toast.makeText(context, "Must select at least one diatonic degree", Toast.LENGTH_SHORT).show()
                            }
                            else if (noteType.value!! == GeneratorNoteType.CHROMATIC && !chromaticDegrees.value!!.contains(true)) {
                                Toast.makeText(context, "Must select at least one chromatic degree", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                viewModel.generator.setupGenerator()
                                val directions = ExerciseSetupFragmentDirections.actionExerciseSetupFragmentToSessionFragment()
                                findNavController().navigate(directions)
                            }
                        },
                        backClickListener = View.OnClickListener {
                            Toast.makeText(context, "back", Toast.LENGTH_SHORT).show()
                        }
                )
        settingItemsArray.add(nextButton)
    }

}
