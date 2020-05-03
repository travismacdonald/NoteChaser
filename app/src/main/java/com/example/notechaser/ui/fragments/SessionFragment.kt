package com.example.notechaser.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.notechaser.R
import com.example.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.example.notechaser.databinding.FragmentSessionBinding
import com.example.notechaser.playablegenerator.SingleNoteGenerator
import com.example.notechaser.viewmodels.ExerciseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ticker
import timber.log.Timber

const val ONE_MINUTE = 60000L
const val ONE_SECOND = 1000L

class SessionFragment : Fragment() {

    val viewModel: ExerciseViewModel by activityViewModels()
    lateinit var binding: FragmentSessionBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Timber.d(
                " \nupper -> ${(viewModel.generator as SingleNoteGenerator).upperBound.value}\n" +
                        "lower -> ${(viewModel.generator as SingleNoteGenerator).lowerBound.value}\n"
        )

        // TODO: delete this line after testing is done
        viewModel.settings.timerLength.value = 1

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_session, container, false
        )
        binding.lifecycleOwner = this

        binding.generateButton.setOnClickListener {
            val playable = viewModel.generator.generatePlayable()
            binding.playableText.text = playable.toString()
            binding.flatText.text = playable.toStringFlat()
            binding.sharpText.text = playable.toStringSharp()
            viewModel.questionsAnswered.value = viewModel.questionsAnswered.value!! + 1
            if (viewModel.questionsAnswered.value!! == viewModel.settings.numQuestions.value!!) {
                // TODO: actually navigate to stat page after
                Toast.makeText(context, "Session Over", Toast.LENGTH_SHORT).show()
            }
        }

        when (viewModel.settings.sessionLengthType.value!!) {
            ExerciseSetupSettings.QUESTION_LIMIT -> {
                viewModel.questionsAnswered.observe(viewLifecycleOwner, Observer {
                    binding.sessionLengthText.text = "$it/${viewModel.settings.numQuestions.value}"
                })
            }
            ExerciseSetupSettings.TIME_LIMIT -> {
                viewModel.startTimer()
                viewModel.secondsPassed.observe(viewLifecycleOwner, Observer {
                    binding.sessionLengthText.text = "${it / 60}:${(it % 60).toString().padStart(2, '0')}/${viewModel.settings.timerLength.value}:00"
                })
            }
            ExerciseSetupSettings.UNLIMITED -> {
                binding.sessionLengthText.text = "Unlimited"
            }
        }

        return binding.root
    }

}
