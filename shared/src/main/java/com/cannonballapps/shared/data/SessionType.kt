package com.cannonballapps.shared.data

enum class SessionType {

    QUESTION_LIMIT {
        override fun toString() = "Question Limit"
    },

    TIME_LIMIT {
        override fun toString() = "Time Limit"
    },

    // TODO: implement "Unlimited"
}
