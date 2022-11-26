package com.cannonballapps.notechaser.exercisesession

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import cn.sherlock.com.sun.media.sound.SF2Soundbank
import cn.sherlock.com.sun.media.sound.SoftSynthesizer
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.common.MidiPlayer2
import com.cannonballapps.notechaser.common.PlayablePlayer
import com.cannonballapps.notechaser.common.SessionType
import com.cannonballapps.notechaser.common.SoundEffectPlayer
import com.cannonballapps.notechaser.databinding.FragmentSessionBinding
import com.cannonballapps.notechaser.musicutilities.Note
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_session.view.*
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber

@ObsoleteCoroutinesApi
@AndroidEntryPoint
class SessionFragment : Fragment() {

    private val viewModel: SessionViewModel by viewModels()
    lateinit var binding: FragmentSessionBinding
    private lateinit var args: SessionFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("backStack count: ${requireActivity().supportFragmentManager.backStackEntryCount}")

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.pauseSession()
            showEndSessionDialog()
        }

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_session,
            container,
            false
        )
        binding.lifecycleOwner = this

        subscribeToLiveData()

        // TODO: feels kind of hacky to have all these calls here
        if (!viewModel.sessionHasStarted) {
            args = SessionFragmentArgs.fromBundle(requireArguments())
            viewModel.initGenerator(args.exerciseType)
            injectPlayablePlayerIntoViewModel()
            injectSoundEffectPlayer()

            viewModel.startSession()
        }

        return binding.root
    }

    private fun subscribeToLiveData() {
        setupSessionStatusMessage()
        setupSessionQuestionCounter()
        setupSessionTimer()
        setupSkipQuestionButton()
        setupReplayQuestionButton()
    }

    private fun setupSkipQuestionButton() {
        binding.playableActionButtonsContainer.skipQuestion_button.apply {
            viewModel.sessionState.observe(viewLifecycleOwner) { state ->
                isEnabled = questionButtonsShouldBeEnabled(state)
            }
            setOnClickListener {
                viewModel.skipQuestion()
            }
        }
    }

    private fun setupReplayQuestionButton() {
        binding.playableActionButtonsContainer.replayQuestion_button.apply {
            viewModel.sessionState.observe(viewLifecycleOwner) { state ->
                isEnabled = questionButtonsShouldBeEnabled(state)
            }
            setOnClickListener {
                viewModel.replayQuestion()
            }
        }
    }

    private fun setupSessionQuestionCounter() {
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

    private fun setupSessionStatusMessage() {
        viewModel.sessionState.observe(viewLifecycleOwner) { state ->
            updateStatusMessageForState(state)
        }
    }

    private fun updateStatusMessageForState(state: SessionViewModel.State) {
        removeStatusMessageObservers()
        when (state) {
            SessionViewModel.State.COUNTDOWN -> {
                setupStatusMessageForCountdown()
            }
            SessionViewModel.State.PLAYING_STARTING_PITCH -> {
                setupStatusMessageForStartingPitch()
            }
            SessionViewModel.State.PLAYING_QUESTION -> {
                setupStatusMessageForPlayingQuestion()
            }
            SessionViewModel.State.LISTENING -> {
                setupStatusMessageForListening()
            }
            SessionViewModel.State.ANSWER_CORRECT -> {
                setupStatusMessageForAnswerCorrect()
            }
            SessionViewModel.State.QUESTION_SKIPPED -> {
                setupStatusMessageForQuestionSkipped()
            }
            SessionViewModel.State.FINISHING -> {
                setupStatusMessageForFinishing()
            }
            SessionViewModel.State.FINISHED -> {
                viewModel.endSession()
                navigateToHomeFragment()
            }
            else -> {
                // TODO Nothing
            }
        }
    }

    private fun setupStatusMessageForAnswerCorrect() {
        binding.sessionStatusMessageTv.apply {
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.sessionText_medium)
            )
            text = getString(R.string.answerCorrect)
        }
    }

    private fun setupStatusMessageForCountdown() {
        binding.sessionStatusMessageTv.apply {
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.sessionText_huge)
            )

            viewModel.secondsUntilSessionStart.observe(viewLifecycleOwner) { seconds ->
                text = seconds.toString()
            }
        }
    }

    private fun setupStatusMessageForFinishing() {
        binding.sessionStatusMessageTv.apply {
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.sessionText_medium)
            )
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.sessionText_medium)
            )
            text = getString(R.string.sessionComplete)
        }
    }

    private fun setupStatusMessageForListening() {
        binding.sessionStatusMessageTv.apply {
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.sessionText_large)
            )

            viewModel.curPitchDetectedAsMidiNumber.observe(viewLifecycleOwner) { midiNum ->
                // TODO: refactor
                val noteStr = if (midiNum == null) "..." else Note(midiNum).toString()
                text = noteStr
            }
        }
    }

    private fun setupStatusMessageForPlayingQuestion() {
        binding.sessionStatusMessageTv.apply {
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.sessionText_medium)
            )
            text = getString(R.string.playingQuestion)
        }
    }

    private fun setupStatusMessageForQuestionSkipped() {
        binding.sessionStatusMessageTv.apply {
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.sessionText_medium)
            )
            text = getString(R.string.questionSkipped)
        }
    }

    private fun setupStatusMessageForStartingPitch() {
        binding.sessionStatusMessageTv.apply {
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.sessionText_medium)
            )
            val pitchClass = viewModel.referencePitch!!.pitchClass
            text = getString(R.string.referencePitch, pitchClass)
        }
    }

    private fun questionButtonsShouldBeEnabled(state: SessionViewModel.State?) =
        state == SessionViewModel.State.LISTENING

    private fun setupSessionTimer() {
        binding.sessionTimeTv.apply {
            viewModel.elapsedSessionTimeInSeconds.observe(viewLifecycleOwner) { totalSeconds ->
                text = when (viewModel.sessionType) {
                    SessionType.TIME_LIMIT -> {
                        val sessionTimeInSeconds = viewModel.sessionTimeLenInMinutes * 60
                        val timeStr = secondsToFormattedTimeString(sessionTimeInSeconds - totalSeconds)
                        timeStr
                    }
                    SessionType.QUESTION_LIMIT -> {
                        val timeStr = secondsToFormattedTimeString(totalSeconds)
                        timeStr
                    }
                }
            }
        }
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

    private fun navigateToHomeFragment() {
        val directions = SessionFragmentDirections.actionSessionToExerciseSelection()
        binding.root.findNavController().navigate(directions)
    }

    private fun secondsToFormattedTimeString(seconds: Int): String {
        val mins = seconds / 60
        val remainingSeconds = seconds % 60
        return "$mins:${"%02d".format(remainingSeconds)}"
    }

    private fun showEndSessionDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.endSessionDialog_title))
            .setMessage(resources.getString(R.string.endSessionDialog_message))
            .setNegativeButton(resources.getString(R.string.endSessionDialog_resume)) { _, _ ->
                Timber.d("session resumed!")
                viewModel.resumeSession()
            }
            .setPositiveButton(resources.getString(R.string.endSessionDialog_end)) { _, _ ->
                Timber.d("session ended!")
                viewModel.endSession()
                navigateToHomeFragment()
            }
            .show()
    }

    private fun removeStatusMessageObservers() {
        viewModel.secondsUntilSessionStart.removeObservers(viewLifecycleOwner)
        viewModel.curPitchDetectedAsMidiNumber.removeObservers(viewLifecycleOwner)
    }
}
