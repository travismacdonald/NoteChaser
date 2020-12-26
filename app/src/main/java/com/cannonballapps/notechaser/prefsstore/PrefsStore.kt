package com.cannonballapps.notechaser.prefsstore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.cannonballapps.notechaser.data.NotePoolType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val DEFAULT_NUM_QUESTIONS = 20

private const val STORE_NAME = "notechaser_data_store"


class PrefsStore(context: Context) {

    private val dataStore = context.createDataStore(
            name = STORE_NAME
    )

    fun notePoolType() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        }
        else {
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

    fun numQuestions() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        }
        else {
            throw exception
        }
    }.map { it[PrefKeys.NUM_QUESTIONS_KEY] ?: DEFAULT_NUM_QUESTIONS }

    suspend fun saveNumQuestions(numQuestions: Int) {
        dataStore.edit {
            it[PrefKeys.NUM_QUESTIONS_KEY] = numQuestions
        }
    }

    private object PrefKeys {
        val NOTE_POOL_TYPE = preferencesKey<Int>("note_pool_type")
        val NUM_QUESTIONS_KEY = preferencesKey<Int>("num_questions")
    }


}