package com.cannonballapps.notechaser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import cn.sherlock.com.sun.media.sound.SF2Soundbank
import cn.sherlock.com.sun.media.sound.SoftSynthesizer
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.databinding.FragmentSessionBinding
import com.cannonballapps.notechaser.models.MidiPlayer2
import com.cannonballapps.notechaser.models.PlayablePlayer
import com.cannonballapps.notechaser.viewmodels.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SessionFragment : Fragment() {

    val viewModel: SessionViewModel by viewModels()
    lateinit var binding: FragmentSessionBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val args = SessionFragmentArgs.fromBundle(requireArguments())
        viewModel.initGenerator(args.exerciseType)
        injectPlayablePlayerIntoViewModel()

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_session, container, false
        )
        binding.lifecycleOwner = this

        binding.randomButton.setOnClickListener {
            viewModel.startSession()
        }

        binding.stopButton.setOnClickListener {
            viewModel.endSession()
        }

        viewModel.curPlayable.observe(viewLifecycleOwner) { playable ->

        }

        viewModel.sessionState.observe(viewLifecycleOwner) { state ->
            binding.sessionStateTv.text = state.toString()
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
    }

    // TODO: this lives here for now; should refactor to use Hilt
    @Suppress("BlockingMethodInNonBlockingContext")
    private fun injectPlayablePlayerIntoViewModel() {
        GlobalScope.launch {
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
}
