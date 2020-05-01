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
import java.util.*
import kotlin.collections.ArrayList

class ExerciseSetupFragment : Fragment() {

    val viewModel: ExerciseSetupViewModel by viewModels {
        InjectorUtils.provideExerciseSetupViewModelFactory(
                ExerciseSetupFragmentArgs.fromBundle(arguments!!).exerciseType
        )
    }
    lateinit var binding: FragmentExerciseSetupBinding
    lateinit var adapter: ExerciseSetupAdapter
    lateinit var args: ExerciseSetupFragmentArgs
    lateinit var settingItemsArray: MutableList<ExerciseSetupItem>

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
        adapter = ExerciseSetupAdapter(this)
        args = ExerciseSetupFragmentArgs.fromBundle(arguments!!)

        // TODO: may have to make this mutable later on
        when (args.exerciseType) {
            ExerciseType.SINGLE_NOTE -> makeSingleNoteList()
            ExerciseType.INTERVALLIC -> makeIntervallicList()
            ExerciseType.HARMONIC -> makeHarmonicList()
            ExerciseType.SCALE -> makeScaleList()
            ExerciseType.MELODIC -> makeMelodicList()
            else -> throw IllegalArgumentException("Unknown exercise type: ${args.exerciseType}")
        }

        adapter.submitList(settingItemsArray)
        binding.settingsList.adapter = adapter
        binding.settingsList.layoutManager = manager

        return binding.root
    }


    private fun makeSingleNoteList() {

        settingItemsArray = arrayListOf()
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
                    // TODO: do better
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
                        // TODO: do better
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

        val questionKeyTitle = resources.getString(R.string.questionKey_title)
        val questionKeySingle: ExerciseSetupItem =
                ExerciseSetupItem.SingleList(
                        questionKeyTitle,
                        MusicTheoryUtils.CHROMATIC_SCALE_FLAT,
                        generator.questionKey,
                        clickListener = View.OnClickListener {
                            var tempItem = generator.questionKey.value!!
                            MaterialAlertDialogBuilder(context!!)
                                    .setTitle(questionKeyTitle)
                                    .setNegativeButton("Dismiss") { _, _ ->
                                        // Do nothing
                                    }
                                    .setPositiveButton("Confirm") { _, _ ->
                                        // Commit Changes
                                        generator.questionKey.value = tempItem
                                    }
                                    .setSingleChoiceItems(MusicTheoryUtils.CHROMATIC_SCALE_FLAT, tempItem) { _, which ->
                                        tempItem = which
                                    }
                                    .show()
                        }
                )


        // TODO: question scale (mixo, dorian, etc ... )

        val parentScaleTitle = resources.getString(R.string.parentScale_title)
        val parentScaleEntries = (MusicTheoryUtils.PARENT_SCALE_BANK.map { it.name }).toTypedArray()
        val parentScaleValue = Transformations.map(generator.parentScale) { value ->
            parentScaleEntries.indexOf(value.name)
        }
        val parentScaleSingle: ExerciseSetupItem =
                ExerciseSetupItem.SingleList(
                        parentScaleTitle,
                        parentScaleEntries,
                        parentScaleValue,
                        clickListener = View.OnClickListener {
                            var tempItem = parentScaleValue.value!!
                            MaterialAlertDialogBuilder(context!!)
                                    .setTitle(parentScaleTitle)
                                    .setNegativeButton("Dismiss") { _, _ ->
                                        // Do nothing
                                    }
                                    .setPositiveButton("Confirm") { _, _ ->
                                        // Commit Changes
                                        generator.parentScale.value = MusicTheoryUtils.PARENT_SCALE_BANK[tempItem]
                                    }
                                    .setSingleChoiceItems(parentScaleEntries, tempItem) { _, which ->
                                        tempItem = which
                                    }
                                    .show()
                        }
                )


        // TODO: question range

        /* Session */

        // TODO: header

        // TODO: session type

        // TODO: session timer

        // TODO: num questions

        /* Answers */

        // TODO: header

        // TODO: match octave

        settingItemsArray.add(questionsHeader)
        settingItemsArray.add(noteChoiceSingle)
        settingItemsArray.add(chromaticMulti)
        settingItemsArray.add(diatonicMulti)
        settingItemsArray.add(questionKeySingle)
        settingItemsArray.add(parentScaleSingle)


        // TODO: this is just here to check for blank space at end
        settingItemsArray.add(questionsHeader)

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
