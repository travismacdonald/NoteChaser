package com.cannonballapps.notechaser.common

import com.cannonballapps.notechaser.musicutilities.playablegenerator.Playable

data class QuestionLog(
    val question: Playable,
    val timeSpentAnsweringInMillis: Long,
    val numberOfRepeats: Int,
    val skipped: Boolean
)
