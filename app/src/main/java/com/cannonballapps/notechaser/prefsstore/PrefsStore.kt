package com.cannonballapps.notechaser.prefsstore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.cannonballapps.notechaser.data.NotePoolType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val STORE_NAME = "notechaser_data_store"

private const val DEFAULT_NUM_QUESTIONS = 20

private val DEFAULT_CHROMATIC_DEGREES = booleanArrayOf(
        true, false, false, false, false, false,
        false, false, false, false, false, false
).joinToString(",")

private val DEFAULT_DIATONIC_DEGREES = booleanArrayOf(
        true, false, false, false,
        false, false, false
).joinToString(",")

private const val DEFAULT_QUESTION_KEY = 0

class PrefsStore(context: Context) {

    private val dataStore = context.createDataStore(
            name = STORE_NAME
    )

    fun notePoolType() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
        it[PrefKeys.NOTE_POOL_TYPE] ?: NotePoolType.DIATONIC.ordinal
    }.map {
        NotePoolType.values()[it]
    }

    suspend fun saveNotePoolType(type: NotePoolType) {
        dataStore.edit {
            it[PrefKeys.NOTE_POOL_TYPE] = type.ordinal
        }
    }

    fun chromaticDegrees() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
        val serializedChromaticDegrees: String = it[PrefKeys.CHROMATIC_DEGREES]
                ?: DEFAULT_CHROMATIC_DEGREES
        deserializeBooleanArray(serializedChromaticDegrees)
    }

    suspend fun saveChromaticDegrees(degrees: BooleanArray) {
        dataStore.edit {
            it[PrefKeys.CHROMATIC_DEGREES] = serializeBooleanArray(degrees)
        }
    }

    fun diatonicDegrees() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
        val serializedDiatonicDegrees: String = it[PrefKeys.DIATONIC_DEGREES]
                ?: DEFAULT_DIATONIC_DEGREES
        deserializeBooleanArray(serializedDiatonicDegrees)
    }

    suspend fun saveDiatonicDegrees(degrees: BooleanArray) {
        dataStore.edit {
            it[PrefKeys.DIATONIC_DEGREES] = serializeBooleanArray(degrees)
        }
    }

    fun questionKey() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
        it[PrefKeys.QUESTION_KEY] ?: DEFAULT_QUESTION_KEY
    }

    suspend fun saveQuestionKey(key: Int) {
        dataStore.edit {
            it[PrefKeys.QUESTION_KEY] = key
        }
    }

    fun numQuestions() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
        it[PrefKeys.NUM_QUESTIONS_KEY] ?: DEFAULT_NUM_QUESTIONS
    }

    suspend fun saveNumQuestions(numQuestions: Int) {
        dataStore.edit {
            it[PrefKeys.NUM_QUESTIONS_KEY] = numQuestions
        }
    }

    private object PrefKeys {
        val NOTE_POOL_TYPE = preferencesKey<Int>("note_pool_type")
        val CHROMATIC_DEGREES = preferencesKey<String>("chromatic_degrees")
        val DIATONIC_DEGREES = preferencesKey<String>("diatonic_degrees")
        val QUESTION_KEY = preferencesKey<Int>("question_key")

        val NUM_QUESTIONS_KEY = preferencesKey<Int>("num_questions")
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