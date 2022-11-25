package com.cannonballapps.notechaser.musicutilities

enum class NotePoolType {

    DIATONIC {
        override fun toString() = "Diatonic"
    },

    CHROMATIC {
        override fun toString() = "Chromatic"
    },
}
