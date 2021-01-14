package com.cannonballapps.notechaser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import cn.sherlock.com.sun.media.sound.SF2Soundbank
import cn.sherlock.com.sun.media.sound.SoftSynthesizer
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.data.SessionType
import com.cannonballapps.notechaser.databinding.FragmentSessionBinding
import com.cannonballapps.notechaser.models.MidiPlayer2
import com.cannonballapps.notechaser.models.PlayablePlayer
import com.cannonballapps.notechaser.models.SoundEffectPlayer
import com.cannonballapps.notechaser.musicutilities.NoteFactory
import com.cannonballapps.notechaser.viewmodels.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_session.view.*

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch


@ObsoleteCoroutinesApi
@AndroidEntryPoint
class SessionFragment : Fragment() {

    private val viewModel: SessionViewModel by viewModels()
    lateinit var binding: FragmentSessionBinding
    private lateinit var args: SessionFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        args = SessionFragmentArgs.fromBundle(requireArguments())
        viewModel.initGenerator(args.exerciseType)
        injectPlayablePlayerIntoViewModel()
        injectSoundEffectPlayer()

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_session, container, false
        )
        binding.lifecycleOwner = this

        subscribeToLiveData()

        return binding.root
    }

    private fun subscribeToLiveData() {

        setupSkipQuestionButton()
        setupQuestionCounterText()


        subscribeToDetectedPitch()

        viewModel.curFilteredNoteDetected.observe(viewLifecycleOwner) { note ->
            // TODO
        }

        subscribeToSessionState()

//        subscribeToNumCorrectAnswers()

        subscribeToSessionElapsedTime()

        viewModel.timeSpentAnsweringCurrentQuestionInMillis.observe(viewLifecycleOwner) { millis ->
            // TODO?
        }

        subscribeToSessionStartCountdown()

//        binding.playableActionButtonsContainer.isVisible = false
    }

    private fun subscribeToSessionElapsedTime() {
        viewModel.elapsedSessionTimeInSeconds.observe(viewLifecycleOwner) { totalSeconds ->
            when (viewModel.sessionType) {
                SessionType.TIME_LIMIT -> {
                    val sessionTimeInSeconds = viewModel.sessionTimeLenInMinutes * 60
                    val timeStr = secondsToFormattedTimeString(sessionTimeInSeconds - totalSeconds)
                    binding.sessionTimeTv.text = timeStr
                }
                SessionType.QUESTION_LIMIT -> {
                    val timeStr = secondsToFormattedTimeString(totalSeconds)
                    binding.sessionTimeTv.text = timeStr
                }
            }
        }
    }

    private fun setupQuestionCounterText() {
        viewModel.numQuestionsCorrect.observe(viewLifecycleOwner) { num ->
            val questionText = when (viewModel.sessionType) {
                SessionType.QUESTION_LIMIT -> {
                    getString(
                            R.string.correctAnswerCounterWithLimit,
                            num,
                            viewModel.numQuestions
                    )
                }
                SessionType.TIME_LIMIT -> {
                    getString(R.string.correctAnswerCounter, num)
                }
            }
            binding.questionCounterTv.text = questionText
        }
    }


    private fun subscribeToSessionStartCountdown() {
        viewModel.secondsUntilSessionStart.observe(viewLifecycleOwner) { seconds ->
            binding.sessionCountdownTv.text = seconds.toString()
        }
    }

    private fun subscribeToDetectedPitch() {
        viewModel.curPitchDetectedAsMidiNumber.observe(viewLifecycleOwner) { midiNum ->
            val noteStr = if (midiNum == null) "..." else NoteFactory.makeNoteFromMidiNumber(midiNum).toString()
            binding.detectedPitchTv.text = noteStr
        }
    }

    private fun subscribeToSessionState() {
        viewModel.sessionState.observe(viewLifecycleOwner) { state ->

            binding.detectedPitchTv.visibility =
                    if (state == SessionViewModel.State.LISTENING) View.VISIBLE else View.INVISIBLE

            binding.sessionCountdownTv.isVisible = state == SessionViewModel.State.COUNTDOWN
            binding.playingQuestionText.isVisible = state == SessionViewModel.State.PLAYING_QUESTION || state == SessionViewModel.State.PLAYING_STARTING_PITCH
            binding.answerCorrectText.isVisible = state == SessionViewModel.State.QUESTION_CORRECT
            binding.questionSkippedTv.isVisible = state == SessionViewModel.State.QUESTION_SKIPPED
            binding.sessionFinishedText.isVisible = state == SessionViewModel.State.FINISHING

            if (state == SessionViewModel.State.FINISHED) {
                navigateToHomeFragment(binding.root)
            }

            // TODO
            if (state == SessionViewModel.State.PLAYING_QUESTION) {
                binding.playingQuestionText.text = "Playing Question"
            }
            else if (state == SessionViewModel.State.PLAYING_STARTING_PITCH) {
                // TODO
                binding.playingQuestionText.text = "Reference Pitch: ${viewModel.referencePitch!!.notes[0].pitchClass.toString()}"
            }
        }
    }

    private fun setupSkipQuestionButton() {
        binding.playableActionButtonsContainer.skipQuestion_button.apply {
            viewModel.sessionState.observe(viewLifecycleOwner) { state ->
                isVisible = state != SessionViewModel.State.COUNTDOWN
                isEnabled = state == SessionViewModel.State.LISTENING
            }

            setOnClickListener {
                viewModel.skipQuestion()
            }

        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.startSession()
    }

    // TODO: this lives here for now; should refactor to use Hilt
    @Suppress("BlockingMethodInNonBlockingContext")
    private fun injectPlayablePlayerIntoViewModel() {
        viewModel.viewModelScope.launch {
            val soundBank = SF2Soundbank(
                    requireActivity().application.assets.open(getString(R.string.soundfont_filename))
            )
            val synth = SoftSynthesizer()
            synth.open()
            synth.loadAllInstruments(soundBank)
            synth.channels[0].programChange(0)
            val midiPlayer = MidiPlayer2(synth)

            viewModel.playablePlayer = PlayablePlayer(midiPlayer)
        }
    }

    // TODO: bad bad i know
    private fun injectSoundEffectPlayer() {
        viewModel.viewModelScope.launch {
            viewModel.soundEffectPlayer = SoundEffectPlayer(requireContext())
        }
    }

    private fun navigateToHomeFragment(view: View) {
        val directions = SessionFragmentDirections.actionSessionToExerciseSelection()
        view.findNavController().navigate(directions)
    }

    private fun secondsToFormattedTimeString(seconds: Int): String {
        val mins = seconds / 60
        val remainingSeconds = seconds % 60
        return "${mins}:${"%02d".format(remainingSeconds)}"
    }

}
