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
import cn.sherlock.com.sun.media.sound.SF2Soundbank
import cn.sherlock.com.sun.media.sound.SoftSynthesizer
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.databinding.FragmentSessionBinding
import com.cannonballapps.notechaser.models.MidiPlayer2
import com.cannonballapps.notechaser.models.PlayablePlayer
import com.cannonballapps.notechaser.models.SoundEffectPlayer
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.NoteFactory
import com.cannonballapps.notechaser.viewmodels.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_session.*

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber

@ObsoleteCoroutinesApi
@AndroidEntryPoint
class SessionFragment : Fragment() {

    val viewModel: SessionViewModel by viewModels()
    lateinit var binding: FragmentSessionBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val args = SessionFragmentArgs.fromBundle(requireArguments())
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

        subscribeToDetectedPitch()

        viewModel.curFilteredNoteDetected.observe(viewLifecycleOwner) { note ->
            // TODO
        }

        subscribeToSessionState()

        viewModel.numCorrectAnswers.observe(viewLifecycleOwner) { num ->
            // TODO
        }

        viewModel.sessionTimeInSeconds.observe(viewLifecycleOwner) { seconds ->
//            Timber.d("elapsed time observed")
//            binding.totalElapsedTime.text = "total elapsed: $seconds"
        }

        viewModel.timeSpentAnsweringCurrentQuestionInMillis.observe(viewLifecycleOwner) { millis ->
            // TODO?
        }

        subscribeToSessionStartCountdown()
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
            binding.detectedPitchTv.isVisible = state == SessionViewModel.State.LISTENING
            binding.sessionCountdownTv.isVisible = state == SessionViewModel.State.COUNTDOWN


            binding.playingQuestionText.isVisible = state == SessionViewModel.State.PLAYING_QUESTION
            binding.answerCorrectText.isVisible = state == SessionViewModel.State.WAITING
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
}
