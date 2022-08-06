package com.cannonballapps.notechaser.prefsstore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.cannonballapps.notechaser.data.SessionType
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.NotePoolType
import com.cannonballapps.notechaser.musicutilities.ParentScale2
import com.cannonballapps.notechaser.musicutilities.PitchClass
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val STORE_NAME = "notechaser_data_store"

private val DEFAULT_CHROMATIC_DEGREES = booleanArrayOf(
    true, true, true, true, true, true,
    true, true, true, true, true, true
).joinToString(",")

private val DEFAULT_DIATONIC_DEGREES = booleanArrayOf(
    true,
    false,
    true,
    false,
    true,
    false,
    false
).joinToString(",")

private const val DEFAULT_NUM_QUESTIONS = 20

private const val DEFAULT_PLAYABLE_LOWER_BOUND_MIDI_NUM = 48

private const val DEFAULT_PLAYABLE_UPPER_BOUND_MIDI_NUM = 72

private const val DEFAULT_QUESTION_KEY_VAL = 0

private const val DEFAULT_SESSION_TIME_LEN = 10

// TODO: implement error handling for erronous values (ex: -1 for mode ix)
// TODO: make PrefsStore an interface for easier unit testing

class PrefsStore @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore(
        name = STORE_NAME
    )

    fun chromaticDegrees() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        val serializedChromaticDegrees: String = prefs[PrefKeys.CHROMATIC_DEGREES]
            ?: DEFAULT_CHROMATIC_DEGREES
        deserializeBooleanArray(serializedChromaticDegrees)
    }

    fun diatonicDegrees() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        val serializedDiatonicDegrees: String = prefs[PrefKeys.DIATONIC_DEGREES]
            ?: DEFAULT_DIATONIC_DEGREES
        deserializeBooleanArray(serializedDiatonicDegrees)
    }

    fun matchOctave() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        prefs[PrefKeys.MATCH_OCTAVE] ?: false
    }

    fun modeIx() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        prefs[PrefKeys.MODE_IX] ?: 0
    }

    fun notePoolType() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        prefs[PrefKeys.NOTE_POOL_TYPE_ORDINAL] ?: NotePoolType.DIATONIC.ordinal
    }.map { ordinal ->
        NotePoolType.values()[ordinal]
    }

    fun numQuestions() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        prefs[PrefKeys.NUM_QUESTIONS_KEY] ?: DEFAULT_NUM_QUESTIONS
    }

    fun parentScale() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        prefs[PrefKeys.PARENT_SCALE_ORDINAL] ?: 0
    }.map { ordinal ->
        ParentScale2.values()[ordinal]
    }

    fun playStartingPitch() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        prefs[PrefKeys.PLAY_STARTING_PITCH] ?: true
    }

    fun playableLowerBound() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        prefs[PrefKeys.PLAYABLE_LOWER_BOUND_MIDI_NUM] ?: DEFAULT_PLAYABLE_LOWER_BOUND_MIDI_NUM
    }.map { midiNumber ->
        Note(midiNumber)
    }

    fun playableUpperBound() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        prefs[PrefKeys.PLAYABLE_UPPER_BOUND_MIDI_NUM] ?: DEFAULT_PLAYABLE_UPPER_BOUND_MIDI_NUM
    }.map { midiNumber ->
        Note(midiNumber)
    }

    fun questionKey() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        prefs[PrefKeys.QUESTION_KEY_VAL] ?: DEFAULT_QUESTION_KEY_VAL
    }.map { keyVal ->
        MusicTheoryUtils.CHROMATIC_PITCH_CLASSES_FLAT[keyVal]
    }

    fun sessionTimeLimit() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        prefs[PrefKeys.SESSION_TIME_LIMIT] ?: DEFAULT_SESSION_TIME_LEN
    }

    fun sessionType() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        prefs[PrefKeys.SESSION_TYPE_ORDINAL] ?: 0
    }.map { ordinal ->
        SessionType.values()[ordinal]
    }

    suspend fun saveChromaticDegrees(degrees: BooleanArray) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.CHROMATIC_DEGREES] = serializeBooleanArray(degrees)
        }
    }

    suspend fun saveDiatonicDegrees(degrees: BooleanArray) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.DIATONIC_DEGREES] = serializeBooleanArray(degrees)
        }
    }

    suspend fun saveMatchOctave(matchOctave: Boolean) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.MATCH_OCTAVE] = matchOctave
        }
    }

    suspend fun saveModeIx(ix: Int) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.MODE_IX] = ix
        }
    }

    suspend fun saveNotePoolType(type: NotePoolType) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.NOTE_POOL_TYPE_ORDINAL] = type.ordinal
        }
    }

    suspend fun saveNumQuestions(numQuestions: Int) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.NUM_QUESTIONS_KEY] = numQuestions
        }
    }

    suspend fun saveParentScale(scale: ParentScale2) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.PARENT_SCALE_ORDINAL] = scale.ordinal
        }
    }

    suspend fun savePlayStartingPitch(playPitch: Boolean) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.PLAY_STARTING_PITCH] = playPitch
        }
    }

    suspend fun savePlayableLowerBound(note: Note) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.PLAYABLE_LOWER_BOUND_MIDI_NUM] = note.midiNumber
        }
    }

    suspend fun savePlayableUpperBound(upperBound: Note) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.PLAYABLE_UPPER_BOUND_MIDI_NUM] = upperBound.midiNumber
        }
    }

    suspend fun saveQuestionKey(key: PitchClass) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.QUESTION_KEY_VAL] = key.value
        }
    }

    suspend fun saveSessionTimeLimit(len: Int) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.SESSION_TIME_LIMIT] = len
        }
    }

    suspend fun saveSessionType(type: SessionType) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.SESSION_TYPE_ORDINAL] = type.ordinal
        }
    }

    private object PrefKeys {
        val CHROMATIC_DEGREES = preferencesKey<String>("chromatic_degrees")
        val DIATONIC_DEGREES = preferencesKey<String>("diatonic_degrees")
        val MATCH_OCTAVE = preferencesKey<Boolean>("match_octave")
        val MODE_IX = preferencesKey<Int>("mode_ix")
        val NOTE_POOL_TYPE_ORDINAL = preferencesKey<Int>("note_pool_type_ordinal")
        val NUM_QUESTIONS_KEY = preferencesKey<Int>("num_questions")
        val PARENT_SCALE_ORDINAL = preferencesKey<Int>("parent_scale_ordinal")
        val PLAY_STARTING_PITCH = preferencesKey<Boolean>("play_starting_pitch")
        val PLAYABLE_LOWER_BOUND_MIDI_NUM = preferencesKey<Int>("playable_lower_bound")
        val PLAYABLE_UPPER_BOUND_MIDI_NUM = preferencesKey<Int>("playable_upper_bound")
        val QUESTION_KEY_VAL = preferencesKey<Int>("question_key")
        val SESSION_TIME_LIMIT = preferencesKey<Int>("session_time_limit")
        val SESSION_TYPE_ORDINAL = preferencesKey<Int>("session_type_ordinal")
    }

    private fun serializeBooleanArray(array: BooleanArray): String {
        // [true, false, true] -> "true,false,true"
        return array.joinToString(separator = ",")
    }

    private fun deserializeBooleanArray(string: String): BooleanArray {
        // "true,false,true" -> [true, false, true]
        return string.split(",").map { it.toBoolean() }.toBooleanArray()
    }
}
