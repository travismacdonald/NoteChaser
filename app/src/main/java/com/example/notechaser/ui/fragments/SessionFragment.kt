package com.example.notechaser.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.notechaser.R
import com.example.notechaser.databinding.FragmentSessionBinding
import com.example.notechaser.playablegenerator.SingleNoteGenerator
import com.example.notechaser.viewmodels.ExerciseSetupViewModel
import timber.log.Timber


class SessionFragment : Fragment() {

    val viewModel: ExerciseSetupViewModel by activityViewModels()
    lateinit var binding: FragmentSessionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Timber.d(
                " \nupper -> ${(viewModel.generator as SingleNoteGenerator).upperBound.value}\n" +
                        "lower -> ${(viewModel.generator as SingleNoteGenerator).lowerBound.value}\n"
        )
        return inflater.inflate(R.layout.fragment_session, container, false)
    }

}
