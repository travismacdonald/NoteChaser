package com.example.notechaser.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.notechaser.R
import com.example.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.example.notechaser.databinding.FragmentSessionBinding
import com.example.notechaser.utilities.MusicTheoryUtils
import com.example.notechaser.viewmodels.ExerciseViewModel


class SessionFragment : Fragment() {

    val viewModel: ExerciseViewModel by activityViewModels()
    lateinit var binding: FragmentSessionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_session, container, false
        )
        binding.lifecycleOwner = this

        viewModel.currentPlayable.observe(viewLifecycleOwner, {
            it?.let {
                viewModel.handlePlayable(it)
            }
        })

        viewModel.currentPitchDetected.observe(viewLifecycleOwner, {
            it?.let {
                if (it == -1) {
                    binding.detectedPitchText.text = "..."
                }
                else {
                    binding.detectedPitchText.text = MusicTheoryUtils.ixToName(it)
                }
            }
        })

        when (viewModel.settings.sessionLengthType.value!!) {

            ExerciseSetupSettings.QUESTION_LIMIT -> {
                viewModel.startTimer()
                viewModel.secondsPassed.observe(viewLifecycleOwner, {
                    binding.secondsPassedText.text = "${it / 60}:${(it % 60).toString().padStart(2, '0')}"
                })
                viewModel.questionsAnswered.observe(viewLifecycleOwner, {
                    binding.questionsAnsweredText.text = "$it/${viewModel.settings.numQuestions.value}"
                    if (viewModel.questionsAnswered.value!! == viewModel.settings.numQuestions.value!!) {
                        viewModel.finishSession()
                        val directions = SessionFragmentDirections.actionSessionFragmentToSessionStatisticsFragment()
                        findNavController().navigate(directions)
                    }
                    else {
                        viewModel.generatePlayable()
                    }
                })
            }

            ExerciseSetupSettings.TIME_LIMIT -> {
                viewModel.startTimer()
                viewModel.secondsPassed.observe(viewLifecycleOwner, {
                    binding.secondsPassedText.text = "${it / 60}:${(it % 60).toString().padStart(2, '0')}/${viewModel.settings.timerLength.value}:00"
                    if (it == viewModel.settings.timerLength.value!! * 60) {
                        viewModel.stopTimer()
                        val directions = SessionFragmentDirections.actionSessionFragmentToSessionStatisticsFragment()
                        findNavController().navigate(directions)
                    }
                })
                viewModel.questionsAnswered.observe(viewLifecycleOwner, {
                    binding.questionsAnsweredText.text = "$it"
                    viewModel.generatePlayable()
                })
            }

            ExerciseSetupSettings.UNLIMITED -> {
                binding.secondsPassedText.text = "Unlimited"
                binding.questionsAnsweredText.text = "Unlimited"
            }
        }

        return binding.root
    }

}
