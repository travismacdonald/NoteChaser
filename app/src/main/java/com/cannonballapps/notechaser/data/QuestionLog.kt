package com.cannonballapps.notechaser.data

import com.cannonballapps.notechaser.playablegenerator.Playable

data class QuestionLog(
    val question: Playable,
    val timeSpentAnsweringInMillis: Long,
    val numberOfRepeats: Int,
    val skipped: Boolean
)
