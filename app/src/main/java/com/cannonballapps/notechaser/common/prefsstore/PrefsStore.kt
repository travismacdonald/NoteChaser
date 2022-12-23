package com.cannonballapps.notechaser.common.prefsstore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cannonballapps.notechaser.common.ExerciseSettings
import com.cannonballapps.notechaser.common.SessionType
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.NotePoolType
import com.cannonballapps.notechaser.musicutilities.ParentScale2
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

private const val STORE_NAME = "notechaser_data_store"

private val DEFAULT_CHROMATIC_DEGREES = booleanArrayOf(
    true, true, true, true, true, true,
    true, true, true, true, true, true,
)

private val DEFAULT_DIATONIC_DEGREES = booleanArrayOf(
    true,
    false,
    true,
    false,
    true,
    false,
    false,
)

private const val DEFAULT_NUM_QUESTIONS = 20

private const val DEFAULT_PLAYABLE_LOWER_BOUND_MIDI_NUM = 48

private const val DEFAULT_PLAYABLE_UPPER_BOUND_MIDI_NUM = 72

private const val DEFAULT_QUESTION_KEY_VAL = 0

private const val DEFAULT_SESSION_TIME_LEN = 10

// TODO: implement error handling for erronous values (ex: -1 for mode ix)
// TODO: make PrefsStore an interface for easier unit testing

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(STORE_NAME)

class PrefsStore @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore

    fun exerciseSettingsFlow(): Flow<ExerciseSettings> = dataStore.data.map { preferences ->
        ExerciseSettings(
            chromaticDegrees = preferences[PrefKeys.CHROMATIC_DEGREES]?.deserializeToBooleanArray() ?: DEFAULT_CHROMATIC_DEGREES,
            diatonicDegrees = preferences[PrefKeys.DIATONIC_DEGREES]?.deserializeToBooleanArray() ?: DEFAULT_DIATONIC_DEGREES,
            modeIx = preferences[PrefKeys.MODE_IX] ?: 0,
            notePoolType = NotePoolType.values()[preferences[PrefKeys.NOTE_POOL_TYPE_ORDINAL] ?: NotePoolType.DIATONIC.ordinal],
            numQuestions = preferences[PrefKeys.NUM_QUESTIONS_KEY] ?: DEFAULT_NUM_QUESTIONS,
            parentScale = ParentScale2.values()[preferences[PrefKeys.PARENT_SCALE_ORDINAL] ?: 0],
            matchOctave = preferences[PrefKeys.MATCH_OCTAVE] ?: false,
            playStartingPitch = preferences[PrefKeys.PLAY_STARTING_PITCH] ?: true,
            playableLowerBound = Note(preferences[PrefKeys.PLAYABLE_LOWER_BOUND_MIDI_NUM] ?: DEFAULT_PLAYABLE_LOWER_BOUND_MIDI_NUM),
            playableUpperBound = Note(preferences[PrefKeys.PLAYABLE_UPPER_BOUND_MIDI_NUM] ?: DEFAULT_PLAYABLE_UPPER_BOUND_MIDI_NUM),
            questionKey = MusicTheoryUtils.CHROMATIC_PITCH_CLASSES_FLAT[preferences[PrefKeys.QUESTION_KEY_VAL] ?: DEFAULT_QUESTION_KEY_VAL],
            sessionTimeLimit = preferences[PrefKeys.SESSION_TIME_LIMIT] ?: DEFAULT_SESSION_TIME_LEN,
            sessionType = SessionType.values()[preferences[PrefKeys.SESSION_TYPE_ORDINAL] ?: 0],
        )
    }

    suspend fun saveExerciseSettings(exerciseSettings: ExerciseSettings) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.CHROMATIC_DEGREES] = exerciseSettings.chromaticDegrees.serialize()
            prefs[PrefKeys.DIATONIC_DEGREES] = exerciseSettings.diatonicDegrees.serialize()
            prefs[PrefKeys.MATCH_OCTAVE] = exerciseSettings.matchOctave
            prefs[PrefKeys.MODE_IX] = exerciseSettings.modeIx
            prefs[PrefKeys.NOTE_POOL_TYPE_ORDINAL] = exerciseSettings.notePoolType.ordinal
            prefs[PrefKeys.NUM_QUESTIONS_KEY] = exerciseSettings.numQuestions
            prefs[PrefKeys.PARENT_SCALE_ORDINAL] = exerciseSettings.parentScale.ordinal
            prefs[PrefKeys.PLAY_STARTING_PITCH] = exerciseSettings.playStartingPitch
            prefs[PrefKeys.PLAYABLE_LOWER_BOUND_MIDI_NUM] = exerciseSettings.playableLowerBound.midiNumber
            prefs[PrefKeys.PLAYABLE_UPPER_BOUND_MIDI_NUM] = exerciseSettings.playableUpperBound.midiNumber
            prefs[PrefKeys.QUESTION_KEY_VAL] = exerciseSettings.questionKey.ordinal
            prefs[PrefKeys.SESSION_TIME_LIMIT] = exerciseSettings.sessionTimeLimit
            prefs[PrefKeys.SESSION_TYPE_ORDINAL] = exerciseSettings.sessionType.ordinal
            Timber.tag("fubar").d("saved!")
        }
    }


    private object PrefKeys {
        val CHROMATIC_DEGREES = stringPreferencesKey("chromatic_degrees")
        val DIATONIC_DEGREES = stringPreferencesKey("diatonic_degrees")
        val MATCH_OCTAVE = booleanPreferencesKey("match_octave")
        val MODE_IX = intPreferencesKey("mode_ix")
        val NOTE_POOL_TYPE_ORDINAL = intPreferencesKey("note_pool_type_ordinal")
        val NUM_QUESTIONS_KEY = intPreferencesKey("num_questions")
        val PARENT_SCALE_ORDINAL = intPreferencesKey("parent_scale_ordinal")
        val PLAY_STARTING_PITCH = booleanPreferencesKey("play_starting_pitch")
        val PLAYABLE_LOWER_BOUND_MIDI_NUM = intPreferencesKey("playable_lower_bound")
        val PLAYABLE_UPPER_BOUND_MIDI_NUM = intPreferencesKey("playable_upper_bound")
        val QUESTION_KEY_VAL = intPreferencesKey("question_key")
        val SESSION_TIME_LIMIT = intPreferencesKey("session_time_limit")
        val SESSION_TYPE_ORDINAL = intPreferencesKey("session_type_ordinal")
    }

    private fun BooleanArray.serialize() = this.joinToString(separator = ",")

    private fun String.deserializeToBooleanArray() = this.split(",").map { it.toBoolean() }.toBooleanArray()
}
