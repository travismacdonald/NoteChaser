package com.cannonballapps.notechaser.common

enum class SessionType {

    QUESTION_LIMIT {
        override fun toString() = "Question Limit"
    },

    TIME_LIMIT {
        override fun toString() = "Time Limit"
    },

    // TODO: implement "Unlimited"
}
