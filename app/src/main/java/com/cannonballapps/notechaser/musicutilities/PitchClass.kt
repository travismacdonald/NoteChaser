package com.cannonballapps.notechaser.musicutilities

const val SHARP = '\u266F'
const val FLAT = '\u266d'

enum class PitchClass(val value: Int) {

    C_FLAT(value = 11) {
        override fun toString() = "C$FLAT"
    },

    C(value = 0) {
        override fun toString() = "C"
    },

    C_SHARP(value = 1) {
        override fun toString() = "C$SHARP"
    },

    D_FLAT(value = 1) {
        override fun toString() = "D$FLAT"
    },

    D(value = 2) {
        override fun toString() = "D"
    },

    D_SHARP(value = 3) {
        override fun toString() = "D$SHARP"
    },

    E_FLAT(value = 3) {
        override fun toString() = "E$FLAT"
    },

    E(value = 4) {
        override fun toString() = "E"
    },

    E_SHARP(value = 5) {
        override fun toString() = "E$SHARP"
    },

    F_FLAT(value = 4) {
        override fun toString() = "F$FLAT"
    },

    F(value = 5) {
        override fun toString() = "F"
    },

    F_SHARP(value = 6) {
        override fun toString() = "F$SHARP"
    },

    G_FLAT(value = 6) {
        override fun toString() = "G$FLAT"
    },

    G(value = 7) {
        override fun toString() = "G"
    },

    G_SHARP(value = 8) {
        override fun toString() = "G$SHARP"
    },

    A_FLAT(value = 8) {
        override fun toString() = "A$FLAT"
    },

    A(value = 9) {
        override fun toString() = "A"
    },

    A_SHARP(value = 10) {
        override fun toString() = "A$SHARP"
    },

    B_FLAT(value = 10) {
        override fun toString() = "B$FLAT"
    },

    B(value = 11) {
        override fun toString() = "B"
    },

    B_SHARP(value = 0) {
        override fun toString() = "B$SHARP"
    }

}