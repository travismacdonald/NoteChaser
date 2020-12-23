package com.cannonballapps.notechaser.prefsstore

import kotlinx.coroutines.flow.Flow

interface PrefsStore {

    fun getNumQuestions() : Flow<Int>

    suspend fun saveNumQuestions(numQuestions: Int)

}