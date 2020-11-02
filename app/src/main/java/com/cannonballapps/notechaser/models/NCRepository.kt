package com.cannonballapps.notechaser.models

import androidx.datastore.DataStore

import java.util.prefs.Preferences

class NCRepository(
        val dataStore: DataStore<Preferences>
) {

    fun getNotePoolType() : Int {
//        val EXAMPLE_COUNTER = preferencesKey<Int>("example_counter")
//        val exampleCounterFlow: Flow<Int> = dataStore.data
//                .map { preferences ->
//                    // No type safety.
//                    preferences[EXAMPLE_COUNTER] ?: 0
//                }
        return -1
    }

    fun saveNotePoolType(notePoolType: Int) {
        // TODO: save to datastore
    }

}