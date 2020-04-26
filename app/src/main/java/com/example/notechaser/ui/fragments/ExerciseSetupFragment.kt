package com.example.notechaser.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notechaser.R
import com.example.notechaser.data.ExerciseType
import com.example.notechaser.data.exercisesetup.*
import com.example.notechaser.databinding.FragmentExerciseSetupBinding
import com.example.notechaser.playablegenerator.GeneratorNoteType
import com.example.notechaser.playablegenerator.SingleNoteGenerator
import com.example.notechaser.utilities.MusicTheoryUtils
import com.example.notechaser.ui.adapters.ExerciseSetupAdapter
import com.example.notechaser.utilities.InjectorUtils
import com.example.notechaser.viewmodels.ExerciseSetupViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ExerciseSetupFragment : Fragment() {

    val viewModel: ExerciseSetupViewModel by viewModels {
        InjectorUtils.provideExerciseSetupViewModelFactory(
                ExerciseSetupFragmentArgs.fromBundle(arguments!!).exerciseType
        )
    }
    lateinit var binding: FragmentExerciseSetupBinding
    lateinit var args: ExerciseSetupFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_exercise_setup, container, false
        )
        binding.lifecycleOwner = this

        // TODO: Test button here! remove later plz
//        binding.testButton.setOnClickListener {
//            Timber.d(
//                    " \nlower = ${viewModel.generator.lowerBound.value!!}" +
//                            "\nupper = ${viewModel.generator.upperBound.value!!}")
//        }

        val manager = LinearLayoutManager(activity)
        val adapter = ExerciseSetupAdapter(this)
        args = ExerciseSetupFragmentArgs.fromBundle(arguments!!)

        // TODO: may have to make this mutable later on
        val setupItemList: List<ExerciseSetupItem> = when (args.exerciseType) {
            ExerciseType.SINGLE_NOTE -> makeSingleNoteList()
            ExerciseType.INTERVALLIC -> makeIntervallicList()
            ExerciseType.HARMONIC -> makeHarmonicList()
            ExerciseType.SCALE -> makeScaleList()
            ExerciseType.MELODIC -> makeMelodicList()
            else -> throw IllegalArgumentException("Unknown exercise type: ${args.exerciseType}")
        }

        adapter.submitList(setupItemList)
        binding.settingsList.adapter = adapter
        binding.settingsList.layoutManager = manager

        return binding.root
    }

    /**
     * Test list.
     */
    private fun makeTestList(): List<ExerciseSetupItem> {

//        viewModel.generator = SingleNoteGenerator()
//        val generator = viewModel.generator as SingleNoteGenerator
//
//        /* Question Settings */
//
//        val questionsHeader: ExerciseSetupItem =
//                ExerciseSetupItem.Header(
//                        ExerciseSetupHeader(getString(R.string.questions_header)))
//
//        val numQuestionsSlider: ExerciseSetupItem =
//                ExerciseSetupItem.Slider(
//                        ExerciseSetupSlider(
//                                // TODO: Extract some of these numbers
//                                getString(R.string.numQuestions_title),
//                                10f,
//                                200f,
//                                viewModel.settings.numQuestions,
//                                Transformations.map(viewModel.settings.numQuestions) { value ->
//                                    value.toString()
//                                },
//                                10f
//                        )
//                )
//
//        val maxIntervalSlider: ExerciseSetupItem =
//                ExerciseSetupItem.Slider(
//                        ExerciseSetupSlider(
//                                "Max Interval",
//                                1f,
//                                // Have to account for inclusive upper bound
//                                (MusicTheoryUtils.CHROMATIC_INTERVAL_NAMES.size - 1).toFloat(),
//                                viewModel.settings.maxIntervalInPattern,
//                                Transformations.map(viewModel.settings.maxIntervalInPattern) { value ->
//                                    MusicTheoryUtils.CHROMATIC_INTERVAL_NAMES[value]
//                                },
//                                1f)
//                        )
//
//        val patternRangeBar: ExerciseSetupItem =
//                ExerciseSetupItem.RangeBar(
//                        ExerciseSetupRangeBar(
//                                "Range Bar",
//                                24f,
//                                88f,
//                                generator.lowerBound,
//                                generator.upperBound,
//                                displayValue = {
//                                    val result = MediatorLiveData<String>()
//                                    val updateRange = {
//                                        val lower = MusicTheoryUtils.ixToName(generator.lowerBound.value!!)
//                                        val upper = MusicTheoryUtils.ixToName(generator.upperBound.value!!)
//                                        result.value = "$lower to $upper"
//                                    }
//                                    result.addSource(generator.lowerBound) { updateRange() }
//                                    result.addSource(generator.upperBound) { updateRange() }
//                                    result
//                                }.invoke()
//                        )
//                )
//
//        val noteChoiceTitle = resources.getString(R.string.noteChoice_title)
//        val noteChoiceArray = resources.getStringArray(R.array.notechoice_entries)
//        val noteChoiceValue = viewModel.settings.noteChoice
//        val noteChoiceList: ExerciseSetupItem =
//                ExerciseSetupItem.SingleList(ExerciseSetupSingleList(
//                        noteChoiceTitle,
//                        noteChoiceArray,
//                        noteChoiceValue,
//                        clickListener = View.OnClickListener {
//                            var tempItem = noteChoiceValue.value!!
//                            MaterialAlertDialogBuilder(context!!)
//                                    .setTitle(noteChoiceTitle)
//                                    .setNegativeButton("Dismiss") { _, _ ->
//                                        // Do nothing
//                                    }
//                                    .setPositiveButton("Confirm") { _, _ ->
//                                        // Commit Changes
//                                        if (noteChoiceValue.value != tempItem) {
//                                            noteChoiceValue.value = tempItem
//                                        }
//                                    }
//                                    .setSingleChoiceItems(noteChoiceArray, tempItem) { _, which ->
//                                        tempItem = which
//                                    }
//                                    .show()
//                        }
//
//                ))
//
//        /* Playback Settings */
//
//        val playbackHeader: ExerciseSetupItem =
//                ExerciseSetupItem.Header(
//                        ExerciseSetupHeader(getString(R.string.playback_header)))
//
//        // TODO: This some ugly ass code
//        val playbackTypeTitle = resources.getString(R.string.playbackType_mel_title)
//        val playbackTypeArr = resources.getStringArray(R.array.playbacktype_mel_entries)
//        val playbackTypeValues = viewModel.settings.playbackTypeMel
//
//        val playbackTypeList: ExerciseSetupItem =
//                ExerciseSetupItem.MultiList(ExerciseSetupMultiList(
//                        playbackTypeTitle,
//                        getString(R.string.playbackType_mel_summary),
//                        playbackTypeArr,
//                        playbackTypeValues,
//                        clickListener = View.OnClickListener {
//                            val tempList = playbackTypeValues.value!!.clone()
//                            MaterialAlertDialogBuilder(context!!)
//                                    .setTitle(noteChoiceTitle)
//                                    .setNegativeButton("Dismiss") { _, _ ->
//                                        // Do nothing
//                                    }
//                                    .setPositiveButton("Confirm") { _, _ ->
//                                        // Commit Changes
//                                        playbackTypeValues.value = tempList.copyOf()
//
//                                    }
//                                    .setMultiChoiceItems(playbackTypeArr, tempList) { _, which, checked ->
//                                        tempList[which] = checked
//                                    }
//                                    .show()
//                        }
//
//                ))
//
//        val playCadenceSwitch: ExerciseSetupItem =
//                ExerciseSetupItem.Switch(ExerciseSetupSwitch(
//                        getString(R.string.playCadence_title),
//                        getString(R.string.playCadence_summary),
//                        viewModel.settings.playCadence,
//                        imgSrc = R.drawable.ic_music_note_black_40dp))
//
//        val matchKeySwitch: ExerciseSetupItem =
//                ExerciseSetupItem.Switch(ExerciseSetupSwitch(
//                        getString(R.string.matchKey_title),
//                        getString(R.string.matchKey_summary),
//                        viewModel.settings.matchKey,
//                        // TODO: arg is mutable only live data; the mediator can probably be removed/simplified
//                        isEnabled = {
//                            val result = MediatorLiveData<Boolean>()
//                            val isEnabled = {
//                                result.value = viewModel.settings.playCadence.value
//                            }
//                            result.addSource(viewModel.settings.playCadence) { isEnabled() }
//                            result
//                        }.invoke(),
//                        imgSrc = R.drawable.ic_music_note_black_40dp)
//                )
//
//        val testSwitch: ExerciseSetupItem =
//                ExerciseSetupItem.Switch(ExerciseSetupSwitch(
//                        "Third Item",
//                        "Suck my balls",
//                        MutableLiveData(false),
//                        isEnabled = {
//                            val result = MediatorLiveData<Boolean>()
//                            val isEnabled = {
//                                val playCadence = viewModel.settings.playCadence.value!!
//                                val matchKey = viewModel.settings.matchKey.value!!
//                                result.value = playCadence && matchKey
//                            }
//                            result.addSource(viewModel.settings.playCadence) { isEnabled() }
//                            result.addSource(viewModel.settings.matchKey) { isEnabled() }
//                            result
//                        }.invoke(),
//                        isVisible = {
//                            val result = MediatorLiveData<Boolean>()
//                            val isVisible = {
//                                val noteChoice = viewModel.settings.noteChoice.value!!
//                                result.value = noteChoice == Constants.NOTE_CHOICE_CHROMATIC
//                            }
//                            result.addSource(viewModel.settings.noteChoice) { isVisible() }
//                            result
//                        }.invoke()
//                )
//                )
//
//
//
//        /* Answer Settings */
//
//
//
//        return arrayListOf(
//                /* Questions */
//                questionsHeader,
//                numQuestionsSlider,
//                maxIntervalSlider,
//                patternRangeBar,
//                noteChoiceList,
//                /* Playback */
//                playbackHeader,
//                playbackTypeList,
//                playCadenceSwitch,
//                matchKeySwitch,
//                testSwitch
//                /* Answers */)

        return arrayListOf()
    }

    private fun makeSingleNoteList(): List<ExerciseSetupItem> {

        viewModel.generator = SingleNoteGenerator()
        val generator = viewModel.generator as SingleNoteGenerator

        /* Questions */

        val questionsHeader: ExerciseSetupItem =
                ExerciseSetupItem.Header(
                        ExerciseSetupHeader(getString(R.string.questions_header))
                )

        val noteChoiceTitle = resources.getString(R.string.noteChoice_title)
        val noteChoiceArray = resources.getStringArray(R.array.notechoice_entries)
        val noteChoiceValue = Transformations.map(generator.noteType) { value ->
            val res: Int = when (value) {
                // TODO: perhaps not the most efficient, but quite readable
                GeneratorNoteType.DIATONIC -> noteChoiceArray.indexOf("Diatonic")
                GeneratorNoteType.CHROMATIC -> noteChoiceArray.indexOf("Chromatic")
                else -> -1
            }
            res
        }
        val noteChoiceList: ExerciseSetupItem =
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
                                        generator.noteType.value = GeneratorNoteType.DIATONIC
                                    }
                                    noteChoiceArray.indexOf("Chromatic") -> {
                                        generator.noteType.value = GeneratorNoteType.CHROMATIC
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


        val chromaticMulti: ExerciseSetupItem =
            ExerciseSetupItem.MultiList(
                "Chromatic Intervals",
                "Intervals to choose from",
                MusicTheoryUtils.CHROMATIC_INTERVAL_NAMES_SINGLE,
                generator.chromaticDegrees,
                clickListener = View.OnClickListener {
                    val tempList = generator.chromaticDegrees.value!!.clone()
                    MaterialAlertDialogBuilder(context!!)
                            .setTitle(noteChoiceTitle)
                            .setNegativeButton("Dismiss") { _, _ ->
                                // Do nothing
                            }
                            .setPositiveButton("Confirm") { _, _ ->
                                // Commit Changes
                                generator.chromaticDegrees.value = tempList.copyOf()

                            }
                            .setMultiChoiceItems(MusicTheoryUtils.CHROMATIC_INTERVAL_NAMES_SINGLE, tempList) { _, which, checked ->
                                tempList[which] = checked
                            }
                            .show()
                },
                isVisible = Transformations.map(generator.noteType) { value ->
                    value == GeneratorNoteType.CHROMATIC
                }
            )


        val diatonicMulti: ExerciseSetupItem =
                ExerciseSetupItem.MultiList(
                        "Diatonic Intervals",
                        "Intervals to choose from",
                        MusicTheoryUtils.DIATONIC_INTERVAL_NAMES_SINGLE,
                        generator.diatonicDegrees,
                        clickListener = View.OnClickListener {
                            val tempList = generator.diatonicDegrees.value!!.clone()
                            MaterialAlertDialogBuilder(context!!)
                                    .setTitle(noteChoiceTitle)
                                    .setNegativeButton("Dismiss") { _, _ ->
                                        // Do nothing
                                    }
                                    .setPositiveButton("Confirm") { _, _ ->
                                        // Commit Changes
                                        generator.diatonicDegrees.value = tempList.copyOf()
                                    }
                                    .setMultiChoiceItems(MusicTheoryUtils.DIATONIC_INTERVAL_NAMES_SINGLE, tempList) { _, which, checked ->
                                        tempList[which] = checked
                                    }
                                    .show()
                        },
                        isVisible = Transformations.map(generator.noteType) { value ->
                            value == GeneratorNoteType.DIATONIC
                        }
                )

        // TODO: question key (disabled if chromatic and all notes chosen)

        // TODO: question scale (mixo, dorian, etc ... )

        // TODO: question range

        /* Session */

        // TODO: header

        // TODO: session type

        // TODO: session timer

        // TODO: num questions

        /* Answers */

        // TODO: header

        // TODO: match octave

        return arrayListOf(
                /* Questions */
                questionsHeader,
                noteChoiceList,
                chromaticMulti,
                diatonicMulti
                /* Session */

                /* Playback */

                /* Answers */)
    }

    // TODO
    private fun makeIntervallicList(): List<ExerciseSetupItem> {
        return ArrayList<ExerciseSetupItem>()
    }

    // TODO
    private fun makeHarmonicList(): List<ExerciseSetupItem> {
        return ArrayList<ExerciseSetupItem>()
    }

    // TODO
    private fun makeScaleList(): List<ExerciseSetupItem> {
        return ArrayList<ExerciseSetupItem>()
    }

    // TODO
    private fun makeMelodicList(): List<ExerciseSetupItem> {
        return ArrayList<ExerciseSetupItem>()
    }

}
