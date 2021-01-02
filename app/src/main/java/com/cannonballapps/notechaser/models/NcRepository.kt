package com.cannonballapps.notechaser.models

import android.util.Log
import androidx.datastore.core.DataStore
import com.cannonballapps.notechaser.data.ExerciseType
import com.cannonballapps.notechaser.data.NotePoolType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

class NcRepository(
//        private val dataStore: DataStore<UserPrefs>
) {

    init {
        Timber.d("i love you")
    }

    // TODO: possiblly change Flow<Int> to Flow<NotePoolType> when all is said and done
//    val userPrefsFlow: Flow<UserPrefs> = dataStore.data.catch { exception ->
//        // dataStore.data throws an IOException when an error is encountered when reading data
//        if (exception is IOException) {
//            // TODO: figure out how to do this with timber
//            Log.e("fuck_you","Error reading sort order preferences.", exception)
//            emit(UserPrefs.getDefaultInstance())
//        } else {
//            throw exception
//        }
//
//    }

    // TODO: i just commented out this code
//    val userPrefsFlow: Flow<UserPrefs> = dataStore.data
//            .catch { exception ->
//                // dataStore.data throws an IOException when an error is encountered when reading data
//                if (exception is IOException) {
//                    Timber.d(exception)
//                    emit(UserPrefs.getDefaultInstance())
//                } else {
//                    throw exception
//                }
//            }

//    suspend fun updateSingleNotePoolType(type: Int) {
//        dataStore.updateData { preferences ->
//            preferences.toBuilder().setSingleNotePoolType(type).build()
//        }
//    }

//    suspend fun updateNotePoolType(exerciseType: ExerciseType, notePoolType: NotePoolType) {
//        when (exerciseType) {
//            ExerciseType.SINGLE_NOTE -> {
//                dataStore.updateData { preferences ->
//                    preferences.toBuilder().setSingleNotePoolType(notePoolType.ordinal).build()
//                }
//                Timber.d("WROTE ${notePoolType}")
//
//            }
//            else ->
//                throw IllegalArgumentException("Illegal argument: $exerciseType given for exerciseType.")
//        }
//
//    }


    fun getSingleNotePoolType() : Int {
//        val EXAMPLE_COUNTER = preferencesKey<Int>("example_counter")
//        val exampleCounterFlow: Flow<Int> = dataStore.data
//                .map { preferences ->
//                    // No type safety.
//                    preferences[EXAMPLE_COUNTER] ?: 0
//                }
        return -1
    }

    fun saveSingleNotePoolType(notePoolType: Int) {
        // TODO: save to datastore
    }

}