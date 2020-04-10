package com.example.notechaser.ui.exerciseconfiguration


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceFragmentCompat
import com.example.notechaser.R
import timber.log.Timber

class ExerciseConfigurationFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_exercise_configuration, rootKey)
        val args = ExerciseConfigurationFragmentArgs.fromBundle(arguments!!)
        Timber.i(args.exerciseType.toString())
    }
}