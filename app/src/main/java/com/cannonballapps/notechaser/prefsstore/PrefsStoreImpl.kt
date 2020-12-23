package com.cannonballapps.notechaser.prefsstore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val STORE_NAME = "notechaser_data_store"


class PrefsStoreImpl(context: Context) : PrefsStore {

    private val dataStore = context.createDataStore(
            name = STORE_NAME
    )

    override fun getNumQuestions() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        }
        else {
            throw exception
        }
        // TODO: save default num questions val as android resource value
    }.map { it[PrefKeys.NUM_QUESTIONS_KEY] ?: 20 }

    override suspend fun saveNumQuestions(numQuestions: Int) {
        dataStore.edit {
            it[PrefKeys.NUM_QUESTIONS_KEY] = numQuestions
        }
    }

    private object PrefKeys {
        val NUM_QUESTIONS_KEY = preferencesKey<Int>("num_questions")
    }


}