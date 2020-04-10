package com.example.notechaser.ui.exerciseconfiguration


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.preference.*
import com.example.notechaser.R
import com.example.notechaser.data.ExerciseType
import com.example.notechaser.databinding.FragmentExerciseSelectionBinding
import com.example.notechaser.patterngenerator.MusicTheory
import timber.log.Timber

class ExerciseConfigurationFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_exercise_configuration, rootKey)

        val exerciseType = ExerciseConfigurationFragmentArgs.fromBundle(arguments!!).exerciseType

        findPreference<ListPreference>(getString(R.string.cadencekey_key))?.apply {
            entries = MusicTheory.CHROMATIC_SCALE_FLAT
            entryValues = MusicTheory.CHROMATIC_SCALE_FLAT
            if (value == null) {
                value = entryValues[0].toString()
            }
        }

        /* TODO: Consider differentiating logic between harmonic modes and melodic modes */
        findPreference<MultiSelectListPreference>(getString(R.string.playbacktype_key))?.apply {
            when (exerciseType) {
                ExerciseType.INTERVALLIC, ExerciseType.HARMONIC -> {
                    isVisible = true
                    entries = entries.sliceArray(0..2)
                    values.clear()
                    values.add(entryValues[2].toString())
                }
                ExerciseType.SCALE, ExerciseType.MELODIC -> {
                    isVisible = true
                    entries = entries.sliceArray(0..1)
                    values.clear()
                    values.add(entryValues[0].toString())
                }
                else -> {
                    isVisible = false
                }
            }
            if (values.isEmpty()) {
                values.add(entryValues[0].toString())
            }
        }

        /* TODO: Fix seekbar to only allow increments of 5 */
//        findPreference<SeekBarPreference>(getString(R.string.timerlength_key))?.apply {
//            Timber.i("made it here")
//            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
//                val seekBar = (preference as SeekBarPreference)
//                seekBar.value = (newValue as Int) * 5
//                true
//            }
//        }

    }

}