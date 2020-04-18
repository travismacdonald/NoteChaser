package com.example.notechaser.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notechaser.R
import com.example.notechaser.data.ExerciseType
import com.example.notechaser.data.exercisesetup.ExerciseSetupHeader
import com.example.notechaser.data.exercisesetup.ExerciseSetupItem
import com.example.notechaser.data.exercisesetup.ExerciseSetupSpinner
import com.example.notechaser.data.exercisesetup.ExerciseSetupSwitch
import com.example.notechaser.databinding.FragmentExerciseSetupBinding
import com.example.notechaser.ui.adapters.ExerciseSetupAdapter
import com.example.notechaser.utilities.InjectorUtils
import com.example.notechaser.viewmodels.ExerciseSetupViewModel
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
                    " \nPlayCadence = ${viewModel.settings.playCadence.value}\n" +
                            "MatchKey =    ${viewModel.settings.matchKey.value}")
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

//        val noteChoiceSpinner: ExerciseSetupItem =
//                ExerciseSetupItem.MultiList(
//
//                )

        val noteChoiceSpinner: ExerciseSetupItem =
                ExerciseSetupItem.Spinner(
                        ExerciseSetupSpinner(
                                getString(R.string.noteChoice_title),
                                arrayListOf("bitch", "lasagna"),
                                arrayListOf()
                        )
                )

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
                        isEnabled = viewModel.settings.playCadence,
                        imgSrc = R.drawable.ic_music_note_black_40dp)
                )

        /* Answer Settings */

        return arrayListOf(
                questionsHeader,
                noteChoiceSpinner,
                playbackHeader,
                playCadenceSwitch,
                matchKeySwitch)
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
