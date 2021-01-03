package com.cannonballapps.notechaser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import cn.sherlock.com.sun.media.sound.SF2Soundbank
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.databinding.FragmentSessionBinding
import com.cannonballapps.notechaser.models.MidiPlayer
import com.cannonballapps.notechaser.models.PlayablePlayer
import com.cannonballapps.notechaser.viewmodels.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

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
            viewModel.getNextPlayable()
        }

        viewModel.curPlayable.observe(viewLifecycleOwner) { playable ->

        }



        return binding.root
    }

    // TODO: this lives here for now; should refactor to use Hilt
    private fun injectPlayablePlayerIntoViewModel() {
        GlobalScope.launch {
            Timber.d("Midi setup started")
            val midiPlayer = MidiPlayer()
            // TODO: fix this warning
            val soundFont = SF2Soundbank(requireActivity().application.assets.open(getString(R.string.soundfont_filename)))
            midiPlayer.sf2 = soundFont
            midiPlayer.setPlugin(0)

            viewModel.playablePlayer = PlayablePlayer(midiPlayer)
            Timber.d("Midi setup complete")
        }
    }
}
