package com.cannonballapps.notechaser.musicutilities

// TODO maybe split this file into smaller util files
object MusicTheoryUtils {

    const val SHARP = '\u266F'
    const val FLAT = '\u266d'
    const val NATURAL = '\u266e'

    const val OCTAVE_SIZE = 12

    val MAJOR_SCALE_INTERVALS = intArrayOf(0, 2, 4, 5, 7, 9, 11)
    val MELODIC_MINOR_SCALE_INTERVALS = intArrayOf(0, 2, 3, 5, 7, 9, 11)
    val HARMONIC_MINOR_SCALE_INTERVALS = intArrayOf(0, 2, 3, 5, 7, 8, 11)
    val HARMONIC_MAJOR_SCALE_INTERVALS = intArrayOf(0, 2, 4, 5, 7, 8, 11)

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

    val HARMONIC_MAJOR_MODE_NAMES = arrayOf(
        "Harmonic Major",
        "Dorian ${FLAT}5",
        "Phrygian ${FLAT}4",
        "Lydian ${FLAT}3",
        "Mixolydian ${FLAT}2",
        "Lydian Augmented ${SHARP}2",
        "Lydian ${FLAT}${FLAT}7"
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

    val CHROMATIC_PITCH_CLASSES_FLAT = arrayOf(
        PitchClass.C,
        PitchClass.D_FLAT,
        PitchClass.D,
        PitchClass.E_FLAT,
        PitchClass.E,
        PitchClass.F,
        PitchClass.G_FLAT,
        PitchClass.G,
        PitchClass.A_FLAT,
        PitchClass.A,
        PitchClass.B_FLAT,
        PitchClass.B
    )

    val CHROMATIC_PITCH_CLASSES_SHARP = arrayOf(
        PitchClass.C,
        PitchClass.C_SHARP,
        PitchClass.D,
        PitchClass.D_SHARP,
        PitchClass.E,
        PitchClass.F,
        PitchClass.F_SHARP,
        PitchClass.G,
        PitchClass.G_SHARP,
        PitchClass.A,
        PitchClass.A_SHARP,
        PitchClass.B
    )

    /**
     * All music theory utils derive note bounds from [MIN_MIDI_NUMBER] and [MAX_MIDI_NUMBER].
     */
    const val MAX_MIDI_NUMBER = 127
    const val MIN_MIDI_NUMBER = 0

    val MAX_PITCH_CLASS = midiNumberToPitchClass(MAX_MIDI_NUMBER)
    val MIN_PITCH_CLASS = midiNumberToPitchClass(MIN_MIDI_NUMBER)

    val MAX_OCTAVE = midiNumberToOctave(MAX_MIDI_NUMBER)
    val MIN_OCTAVE = midiNumberToOctave(MIN_MIDI_NUMBER)

    fun midiNumberToNoteName(midiNumber: Int, asFlat: Boolean = true): String {
        if (asFlat) {
            return "${CHROMATIC_SCALE_FLAT[midiNumber % OCTAVE_SIZE]}${(midiNumber / OCTAVE_SIZE) - 1}"
        }
        return "${CHROMATIC_SCALE_SHARP[midiNumber % OCTAVE_SIZE]}${(midiNumber / OCTAVE_SIZE) - 1}"
    }

    fun midiNumberToPitchClass(midiNumber: Int, preferFlat: Boolean = true): PitchClass {
        val pitchClassIx = midiNumber % OCTAVE_SIZE
        return if (preferFlat) {
            CHROMATIC_PITCH_CLASSES_FLAT[pitchClassIx]
        } else {
            CHROMATIC_PITCH_CLASSES_SHARP[pitchClassIx]
        }
    }

    fun pitchClassAndOctaveToMidiNumber(pitchClass: PitchClass, octave: Int): Int {
        return ((octave + 1) * OCTAVE_SIZE) + pitchClass.value
    }

    fun midiNumberToOctave(midiNumber: Int): Int {
        return (midiNumber / OCTAVE_SIZE) - 1
    }

    fun isValidMidiNumber(midiNumber: Int) = midiNumber in MIN_MIDI_NUMBER..MAX_MIDI_NUMBER

    fun getIntervalsForModeAtIx(intervals: IntArray, modeIx: Int): IntArray {
        if (intervals[0] != 0) {
            throw IllegalArgumentException(
                "Cannot use scale when first interval is not zero.\n" +
                    "Intervals = $intervals"
            )
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

    fun transformChromaticDegreesToIntervals(chromaticDegrees: BooleanArray, key: Int): IntArray {
        if (chromaticDegrees.size != OCTAVE_SIZE) {
            throw IllegalArgumentException(
                "chromaticDegrees must have size 12. actual size = ${chromaticDegrees.size}"
            )
        }
        if (key !in 0..11) {
            throw IllegalArgumentException(
                "key must be in range 0 (inclusive) to 11 (inclusive). actual key = $key"
            )
        }
        val toReturn = IntArray(chromaticDegrees.count() { it })
        var arrayIx = 0
        for (it in chromaticDegrees.withIndex()) {
            if (it.value) {
                toReturn[arrayIx] = it.index
                arrayIx++
            }
        }
        return transposeIntervals(toReturn, key)
    }

    fun transformDiatonicDegreesToIntervals(diatonicDegrees: BooleanArray, scale: IntArray, key: Int): IntArray {
        if (diatonicDegrees.size != scale.size) {
            throw IllegalArgumentException(
                "diatonicDegrees and scale must have same size.\n " +
                    "diatonicDegrees size = ${diatonicDegrees.size}\n" +
                    "scale size = ${scale.size}"
            )
        }
        if (key !in 0..11) {
            throw IllegalArgumentException(
                "key must be in range 0 (inclusive) to 11 (inclusive).\n" +
                    "actual key = $key"
            )
        }
        val toReturn = IntArray(diatonicDegrees.count() { it })
        var arrayIx = 0
        for (it in diatonicDegrees.withIndex()) {
            if (it.value) {
                toReturn[arrayIx] = scale[it.index]
                arrayIx++
            }
        }
        return transposeIntervals(toReturn, key)
    }

    // TODO: better function name
    fun transposeIntervals(intervals: IntArray, key: Int): IntArray {
        if (key !in 0..11) {
            throw IllegalArgumentException(
                "key must be in range 0 (inclusive) to 11 (inclusive).\n" +
                    "actual key = $key"
            )
        }
        val toReturn = IntArray(intervals.size)
        var arrayIx = 0
        val offset = intervals.count { (it + key) < OCTAVE_SIZE }
        for (i in offset until intervals.size) {
            toReturn[arrayIx] = (intervals[i] + key) % OCTAVE_SIZE
            arrayIx++
        }
        for (i in 0 until offset) {
            toReturn[arrayIx] = intervals[i] + key
            arrayIx++
        }
        return toReturn
    }

    // TODO: update it to use new classes `PitchClass` and `Note`
    fun pitchClassOccursBetweenNoteBounds(
        pitchClass: PitchClass,
        lowerBound: Note,
        upperBound: Note
    ): Boolean {
        assertValidBounds(lowerBound, upperBound)

        val numOctavesDisplaced = lowerBound.midiNumber / OCTAVE_SIZE
        val transposedLower = lowerBound.midiNumber - (OCTAVE_SIZE * numOctavesDisplaced)
        val transposedUpper = upperBound.midiNumber - (OCTAVE_SIZE * numOctavesDisplaced)
        if (pitchClass.value in transposedLower..transposedUpper ||
            pitchClass.value + OCTAVE_SIZE in transposedLower..transposedUpper
        ) {
            return true
        }
        return false
    }

    fun getNumberOfPitchClassOccurrencesBetweenBounds(
        pitchClass: PitchClass,
        lowerBound: Note,
        upperBound: Note
    ): Int {
        var numOccurrences = 0
        var curMidiNumber = pitchClass.value + (lowerBound.midiNumber / OCTAVE_SIZE) * OCTAVE_SIZE
        while (curMidiNumber <= upperBound.midiNumber) {
            if (curMidiNumber >= lowerBound.midiNumber) {
                numOccurrences++
            }
            curMidiNumber += OCTAVE_SIZE
        }
        return numOccurrences
    }

    fun getLowestPitchClassOccurrenceBetweenBoundsOrNull(
        pitchClass: PitchClass,
        lowerBound: Note,
        upperBound: Note
    ): Note? {
        if (!pitchClassOccursBetweenNoteBounds(pitchClass, lowerBound, upperBound)) {
            return null
        }
        var curMidiNumber = pitchClass.value
        while (curMidiNumber < lowerBound.midiNumber) {
            curMidiNumber += OCTAVE_SIZE
        }
        return Note(curMidiNumber)
    }

    private fun assertValidBounds(lowerBound: Note, upperBound: Note) {
        if (lowerBound > upperBound) {
            throw IllegalArgumentException(
                "lowerBound must be less than or equal to upperBound"
            )
        }
    }
}
