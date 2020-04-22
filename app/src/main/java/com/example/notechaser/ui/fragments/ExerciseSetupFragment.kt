package com.example.notechaser.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notechaser.R
import com.example.notechaser.data.ExerciseType
import com.example.notechaser.data.exercisesetup.*
import com.example.notechaser.databinding.FragmentExerciseSetupBinding
import com.example.notechaser.ui.adapters.ExerciseSetupAdapter
import com.example.notechaser.utilities.InjectorUtils
import com.example.notechaser.viewmodels.ExerciseSetupViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber

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

        binding.testButton.setOnClickListener {
            Timber.d(
                    "\nNoteChoice = ${viewModel.settings.noteChoice.value}\n")
        }

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
     * Setup adapter for single note settings.
     */
    private fun makeSingleNoteList(): List<ExerciseSetupItem> {

        /* Question Settings */

        val questionsHeader: ExerciseSetupItem =
                ExerciseSetupItem.Header(
                        ExerciseSetupHeader(getString(R.string.questions_header)))

        val noteChoiceTitle = resources.getString(R.string.noteChoice_title)
        val noteChoiceArray = resources.getStringArray(R.array.notechoice_entries)
        val itemChecked = viewModel.settings.noteChoice
        var currentItemChecked = itemChecked.value!!
        val noteChoiceList: ExerciseSetupItem =
                ExerciseSetupItem.SingleList(ExerciseSetupSingleList(
                        noteChoiceTitle,
                        "selected value here",
                        noteChoiceArray,
                        itemChecked,
                        clickListener = View.OnClickListener {
                            MaterialAlertDialogBuilder(context)
                                    .setTitle(noteChoiceTitle)
                                    .setNegativeButton("Dismiss") { dialog, which ->
                                        // Do nothing
                                    }
                                    .setPositiveButton("Confirm") { dialog, which ->
                                        // Commit Changes
                                        if (itemChecked.value != currentItemChecked) {
                                            itemChecked.value = currentItemChecked
                                        }
                                    }
                                    .setSingleChoiceItems(noteChoiceArray, itemChecked.value!!) { dialog, which ->
                                        currentItemChecked = which
                                    }
                                    .show()
                        }

                ))

        /* Playback Settings */

        val playbackHeader: ExerciseSetupItem =
                ExerciseSetupItem.Header(
                        ExerciseSetupHeader(getString(R.string.playback_header)))

        val playCadenceSwitch: ExerciseSetupItem =
                ExerciseSetupItem.Switch(ExerciseSetupSwitch(
                        getString(R.string.playCadence_title),
                        getString(R.string.playCadence_summary),
                        viewModel.settings.playCadence,
                        imgSrc = R.drawable.ic_music_note_black_40dp))

        val matchKeySwitch: ExerciseSetupItem =
                ExerciseSetupItem.Switch(ExerciseSetupSwitch(
                        getString(R.string.matchKey_title),
                        getString(R.string.matchKey_summary),
                        viewModel.settings.matchKey,
                        isEnabled = {
                            val result = MediatorLiveData<Boolean>()
                            val isEnabled = {
                                result.value = viewModel.settings.playCadence.value
                            }
                            result.addSource(viewModel.settings.playCadence) { isEnabled() }
                            result
                        }.invoke(),
                        imgSrc = R.drawable.ic_music_note_black_40dp)
                )

        val testSwitch: ExerciseSetupItem =
                ExerciseSetupItem.Switch(ExerciseSetupSwitch(
                        "Third Item",
                        "Suck my balls",
                        MutableLiveData(false),
                        isEnabled = {
                            val result = MediatorLiveData<Boolean>()
                            val isEnabled = {
                                val playCadence = viewModel.settings.playCadence.value!!
                                val matchKey = viewModel.settings.matchKey.value!!
                                result.value = playCadence && matchKey
                            }
                            result.addSource(viewModel.settings.playCadence) { isEnabled() }
                            result.addSource(viewModel.settings.matchKey) { isEnabled() }
                            result
                        }.invoke(),
                        isVisible = {
                            val result = MediatorLiveData<Boolean>()
                            val isVisible = {
                                val noteChoice = viewModel.settings.noteChoice.value!!
                                result.value = noteChoice == Constants.NOTE_CHOICE_CHROMATIC
                            }
                            result.addSource(viewModel.settings.noteChoice) { isVisible() }
                            result
                        }.invoke()
                )
                )

        /* Answer Settings */

        return arrayListOf(
                questionsHeader,
                noteChoiceList,
                playbackHeader,
                playCadenceSwitch,
                matchKeySwitch,
                testSwitch)
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
