package com.example.notechaser.patterngenerator

class MusicTheory {
    companion object {
        const val SHARP = '\u266F'
        const val FLAT = '\u266d'
        const val NATURAL = '\u266e'

        const val CHROMATIC_SCALE_SIZE = 12;

        val MAJOR_SCALE_SEQUENCE = intArrayOf(0, 2, 4, 5, 7, 9, 11)
        val MELODIC_MINOR_SCALE_SEQUENCE = intArrayOf(0, 2, 3, 5, 7, 9, 11)
        val HARMONIC_MINOR_SCALE_SEQUENCE = intArrayOf(0, 2, 3, 5, 7, 8, 11)

        val CHROMATIC_SCALE_SHARP = arrayOf(
                "C",
                "C$SHARP",
                "D",
                "D$SHARP",
                "E",
                "F",
                "F$SHARP",
                "G",
                "G$SHARP",
                "A",
                "A$SHARP",
                "B"
        )

        val CHROMATIC_SCALE_FLAT = arrayOf(
                "C",
                "D$FLAT",
                "D",
                "E$FLAT",
                "E",
                "F",
                "G$FLAT",
                "G",
                "A$FLAT",
                "A",
                "B$FLAT",
                "B"
        )

        val MAJOR_MODE_NAMES = arrayOf(
                "Ionian",
                "Dorian",
                "Phrygian",
                "Lydian",
                "Mixolydian",
                "Aeolian",
                "Locrian"
        )

        val MELODIC_MINOR_MODE_NAMES = arrayOf(
                "Melodic Minor",
                "Phrygian ${SHARP}6",
                "Lydian Augmented",
                "Lydian ${FLAT}7",
                "Mixolydian ${FLAT}6",
                "Locrian ${SHARP}2",
                "Altered"
        )

        val HARMONIC_MINOR_MODE_NAMES = arrayOf(
                "Harmonic Minor",
                "Locrian ${NATURAL}6",
                "Ionian ${SHARP}5",
                "Dorian ${SHARP}4",
                "Phrygian Dominant",
                "Lydian ${SHARP}9",
                "Altered Diminished"
        )
    }
}