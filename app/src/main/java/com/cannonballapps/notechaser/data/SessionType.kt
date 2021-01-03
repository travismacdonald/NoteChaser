package com.cannonballapps.notechaser.data

enum class SessionType {

    QUESTION_LIMIT {
        override fun toString() = "Question Limit"
    },

    TIME_LIMIT {
        override fun toString() = "Time Limit"
    },

    UNLIMITED {
        override fun toString() = "Unlimited"
    },

}