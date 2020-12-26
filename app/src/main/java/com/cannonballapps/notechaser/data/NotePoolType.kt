package com.cannonballapps.notechaser.data

enum class NotePoolType {
    DIATONIC {
        override fun toString(): String {
            return "Diatonic"
        }
    },
    CHROMATIC {
        override fun toString(): String {
            return "Chromatic"
        }
    }
}