package com.example.notechaser.ui.exerciseconfiguration


import android.os.Bundle
import androidx.preference.*
import com.example.notechaser.R
import com.example.notechaser.data.ExerciseType
import com.example.notechaser.patterngenerator.MusicTheory
import timber.log.Timber

class ExerciseConfigurationFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_exercise_configuration, rootKey)

        val exerciseType = ExerciseConfigurationFragmentArgs.fromBundle(arguments!!).exerciseType

        val matchKeySwitch: SwitchPreferenceCompat =
                findPreference(getString(R.string.matchkey_key))!!
        val cadenceKeyList: ListPreference =
                findPreference(getString(R.string.cadencekey_key))!!
        val playbackTypeMulti: MultiSelectListPreference =
                findPreference(getString(R.string.playbacktype_key))!!
        val playCadenceSwitch: SwitchPreferenceCompat =
                findPreference(getString(R.string.playcadence_key))!!

        /* Cadence Key List */
        cadenceKeyList.apply {
            entries = MusicTheory.CHROMATIC_SCALE_FLAT
            entryValues = MusicTheory.CHROMATIC_SCALE_FLAT
            if (value == null) {
                value = entryValues[0].toString()
            }
            isEnabled = playCadenceSwitch.isChecked && !matchKeySwitch.isChecked
        }

        /* TODO: Consider differentiating logic between harmonic modes and melodic modes */
        playbackTypeMulti.apply {
            when (exerciseType) {
                ExerciseType.INTERVALLIC, ExerciseType.HARMONIC -> {
                    isVisible = true
                    entries = entries.sliceArray(0..2)
                    values.clear()
                    values.add(entryValues[2].toString())
                }
                ExerciseType.SCALE -> {
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

        matchKeySwitch.apply {
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                cadenceKeyList.apply {
                    when (newValue) {
                        false -> isEnabled = true
                        true -> isEnabled = false
                    }
                }
                true
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