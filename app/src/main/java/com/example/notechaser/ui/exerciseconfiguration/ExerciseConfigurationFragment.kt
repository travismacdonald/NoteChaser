package com.example.notechaser.ui.exerciseconfiguration


import android.os.Bundle
import androidx.preference.*
import com.example.notechaser.R
import com.example.notechaser.data.ExerciseType
import com.example.notechaser.patterngenerator.MusicTheory

class ExerciseConfigurationFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_exercise_configuration, rootKey)

        val exerciseType = ExerciseConfigurationFragmentArgs.fromBundle(arguments!!).exerciseType

        val noteChoiceDropDown: DropDownPreference =
                findPreference(getString(R.string.notechoice_key))!!
        val sessionLengthDropDown: DropDownPreference =
                findPreference(getString(R.string.sessionlength_key))!!
        val numQuestionsSeekBar: SeekBarPreference =
                findPreference(getString(R.string.numquestions_key))!!
        val timerLengthSeekBar: SeekBarPreference =
                findPreference(getString(R.string.timerlength_key))!!
        val playbackTypeHarmMulti: MultiSelectListPreference =
                findPreference(getString(R.string.playbacktype_harm_key))!!
        val playbackTypeMelMulti: MultiSelectListPreference =
                findPreference(getString(R.string.playbacktype_mel_key))!!
        val playCadenceSwitch: SwitchPreferenceCompat =
                findPreference(getString(R.string.playcadence_key))!!
        val matchKeySwitch: SwitchPreferenceCompat =
                findPreference(getString(R.string.matchkey_key))!!
        val cadenceKeyList: ListPreference =
                findPreference(getString(R.string.cadencekey_key))!!

        noteChoiceDropDown.apply {
            val noteChoiceArray = resources.getStringArray(R.array.notechoice_values)
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                when (newValue) {
                    /* noteChoiceArray[0] == Diatonic */
                    noteChoiceArray[0] -> {
                        matchKeySwitch.isVisible = true
                        if (matchKeySwitch.isChecked) { cadenceKeyList.isEnabled = false }
                    }
                    /* noteChoiceArray[1] == Chromatic */
                    noteChoiceArray[1] -> {
                        matchKeySwitch.isVisible = false
                        if (playCadenceSwitch.isChecked) { cadenceKeyList.isEnabled = true }
                    }
                }
                true
            }
            this.callChangeListener(value)
        }

        sessionLengthDropDown.apply {
            val valueArray = resources.getStringArray(R.array.sessionlength_values)
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                when (newValue) {
                    /* valueArray[0] == Question Limit */
                    valueArray[0] -> {
                        numQuestionsSeekBar.isVisible = true
                        timerLengthSeekBar.isVisible = false
                    }
                    /* valueArray[0] == Time Limit */
                    valueArray[1] -> {
                        numQuestionsSeekBar.isVisible = false
                        timerLengthSeekBar.isVisible = true
                    }
                    /* valueArray[2] == Unlimited */
                    valueArray[2] -> {
                        numQuestionsSeekBar.isVisible = false
                        timerLengthSeekBar.isVisible = false
                    }
                }
                cadenceKeyList.apply {
                    when (newValue) {
                        false -> isEnabled = true
                        true -> isEnabled = false
                    }
                }
                true
            }
            this.callChangeListener(value)
        }

        playbackTypeHarmMulti.apply {
            when (exerciseType) {
                ExerciseType.INTERVALLIC, ExerciseType.HARMONIC -> {
                    isVisible = true
                    if (values.isEmpty()) {
                        values.add(entryValues[0].toString())
                    }
                }
                else -> {
                    isVisible = false
                }
            }
        }

        playbackTypeMelMulti.apply {
            when (exerciseType) {
                ExerciseType.SCALE -> {
                    isVisible = true
                    if (values.isEmpty()) {
                        values.add(entryValues[0].toString())
                    }
                }
                else -> {
                    isVisible = false
                }
            }
        }

        cadenceKeyList.apply {
            entries = MusicTheory.CHROMATIC_SCALE_FLAT
            entryValues = MusicTheory.CHROMATIC_SCALE_FLAT
            if (value == null) {
                value = entryValues[0].toString()
            }
            isEnabled = playCadenceSwitch.isChecked && !matchKeySwitch.isChecked
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