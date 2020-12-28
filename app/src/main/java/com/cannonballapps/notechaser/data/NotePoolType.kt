package com.cannonballapps.notechaser.data

enum class NotePoolType {

    DIATONIC {
        override fun toString() = "Diatonic"
    },

    CHROMATIC {
        override fun toString() = "Chromatic"
    },

}