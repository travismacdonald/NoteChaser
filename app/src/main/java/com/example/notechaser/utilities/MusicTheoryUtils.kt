package com.example.notechaser.utilities

import com.example.notechaser.playablegenerator.ParentScale
import java.lang.IllegalArgumentException

class MusicTheoryUtils {

    companion object {

        const val SHARP = '\u266F'
        const val FLAT = '\u266d'
        const val NATURAL = '\u266e'

        const val OCTAVE_SIZE = 12

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

        val CHROMATIC_INTERVAL_NAMES_SINGLE = arrayOf(
                "P1",
                "m2",
                "M2",
                "m3",
                "M3",
                "P4",
                "Tritone",
                "P5",
                "m6",
                "M6",
                "m7",
                "M7"
        )

        val DIATONIC_INTERVAL_NAMES_SINGLE = arrayOf(
                "1st",
                "2nd",
                "3rd",
                "4th",
                "5th",
                "6th",
                "7th"
        )

        // TODO: Replace with actual DB later
        val PARENT_SCALE_BANK = arrayOf(
                ParentScale("Major", MAJOR_SCALE_SEQUENCE),
                ParentScale("Melodic Minor", MELODIC_MINOR_SCALE_SEQUENCE),
                ParentScale("Harmonic Minor", HARMONIC_MINOR_SCALE_SEQUENCE)
        )

        fun ixToName(ix: Int): String {
            return "${CHROMATIC_SCALE_FLAT[ix % 12]}${(ix / OCTAVE_SIZE) - 1}"
        }

        fun getModeIntervals(intervals: IntArray, modeIx: Int): IntArray {
            if (intervals[0] != 0) {
                throw IllegalArgumentException(
                        "Cannot use scale when first interval is not zero.\n" +
                        "Intervals = $intervals")
            }
            // No need to change anything
            if (modeIx == 0) {
                return intervals
            }
            val toReturn = IntArray(intervals.size)
            val offset = intervals[modeIx]
            var counter = 0
            for (i in modeIx until toReturn.size) {
                toReturn[counter] = intervals[i] - offset
                ++counter
            }
            for (i in 0 until modeIx) {
                toReturn[counter] = intervals[i] + OCTAVE_SIZE - offset
                ++counter
            }
            return toReturn
        }

    }
}