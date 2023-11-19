package com.cannonballapps.notechaser.common.prefsstore

import android.content.Context
import android.util.Range
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cannonballapps.notechaser.common.ExerciseSettings
import com.cannonballapps.notechaser.common.NotePoolType
import com.cannonballapps.notechaser.common.ResultOf
import com.cannonballapps.notechaser.common.SessionLengthSettings
import com.cannonballapps.notechaser.common.SessionQuestionSettings
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.ParentScale
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.Scale
import com.cannonballapps.notechaser.musicutilities.pitchClassFlat
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlayableGenerator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val STORE_NAME = "notechaser_data_store"

private val DEFAULT_CHROMATIC_DEGREES = booleanArrayOf(
    true, true, true, true, true, true,
    true, true, true, true, true, true,
)

private const val DEFAULT_PLAYABLE_LOWER_BOUND_MIDI_NUM = 48

private const val DEFAULT_PLAYABLE_UPPER_BOUND_MIDI_NUM = 72

// TODO: implement error handling for erronous values (ex: -1 for mode ix)
// TODO: make PrefsStore an interface for easier unit testing

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(STORE_NAME)

class PrefsStore @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val dataStore = context.dataStore

    fun exerciseSettingsFlow(): Flow<ExerciseSettings> = dataStore.data.map { _ ->
        ExerciseSettings(
            notePoolType = NotePoolType.Chromatic(
                degrees = DEFAULT_CHROMATIC_DEGREES,
            ),
            sessionQuestionSettings = SessionQuestionSettings(
                questionKey = PitchClass.C,
                questionKeyValues = pitchClassFlat,
                shouldMatchOctave = false,
                shouldPlayStartingPitch = true,
                playableBounds = Range(
                    Note(midiNumber = DEFAULT_PLAYABLE_LOWER_BOUND_MIDI_NUM),
                    Note(midiNumber = DEFAULT_PLAYABLE_UPPER_BOUND_MIDI_NUM),
                ),
            ),
            sessionLengthSettings = SessionLengthSettings.QuestionLimit(
                numQuestions = 20,
            ),
        )
    }

    fun playableGeneratorFlow(): Flow<ResultOf<PlayableGenerator>> = flow { /* TODO */ }

    suspend fun saveExerciseSettings(exerciseSettings: ExerciseSettings) {
        dataStore.edit { prefs ->
            // todo fix during room refactor
        }
    }

    private object PrefKeys {
        val CHROMATIC_DEGREES = stringPreferencesKey("chromatic_degrees")
        val DIATONIC_DEGREES = stringPreferencesKey("diatonic_degrees")
        val MATCH_OCTAVE = booleanPreferencesKey("match_octave")
        val SCALE_KEY = stringPreferencesKey("scale_key")
        val NOTE_POOL_TYPE_ORDINAL = intPreferencesKey("note_pool_type_ordinal")
        val NUM_QUESTIONS_KEY = intPreferencesKey("num_questions")
        val PLAY_STARTING_PITCH = booleanPreferencesKey("play_starting_pitch")
        val PLAYABLE_LOWER_BOUND_MIDI_NUM = intPreferencesKey("playable_lower_bound")
        val PLAYABLE_UPPER_BOUND_MIDI_NUM = intPreferencesKey("playable_upper_bound")
        val QUESTION_KEY_VAL = intPreferencesKey("question_key")
        val SESSION_TIME_LIMIT = intPreferencesKey("session_time_limit")
        val SESSION_TYPE_ORDINAL = intPreferencesKey("session_type_ordinal")
    }

    private fun BooleanArray.serialize() = this.joinToString(separator = ",")

    private fun Scale.serialize() = this.toString()

    private fun String.deserializeToBooleanArray() = this.split(",").map { it.toBoolean() }.toBooleanArray()

    // todo doesn't work until room refactor
    private fun String.deserializeToScale() = ParentScale.Major.scaleAtIndex(0)
}
