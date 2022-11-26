package com.cannonballapps.shared.data

import com.cannonballapps.musictheory.musicutilities.playablegenerator.Playable


data class QuestionLog(
    val question: Playable,
    val timeSpentAnsweringInMillis: Long,
    val numberOfRepeats: Int,
    val skipped: Boolean
)
