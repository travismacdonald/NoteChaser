package com.cannonballapps.notechaser.musicutilities

import org.junit.Assert.assertArrayEquals
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// TODO: clean up test names; make them more consistent
class MusicTheoryUtilsTest {

    @Test
    fun `test getLowestPitchClassOccurrenceBetweenBoundsOrNull c between 0 and 0 returns c-1`() {
        val lower = Note(0)
        val upper = Note(0)
        val note = MusicTheoryUtils.getLowestPitchClassOccurrenceBetweenBoundsOrNull(
            PitchClass.C,
            lower,
            upper,
        )

        val expected = Note(
            PitchClass.C,
            octave = -1,
        )
        assertEquals(expected, note)
    }

    @Test
    fun `test getLowestPitchClassOccurrenceBetweenBoundsOrNull c between 1 and 12 returns c0`() {
        val lower = Note(1)
        val upper = Note(12)
        val note = MusicTheoryUtils.getLowestPitchClassOccurrenceBetweenBoundsOrNull(
            PitchClass.C,
            lower,
            upper,
        )

        val expected = Note(
            PitchClass.C,
            octave = 0,
        )
        assertEquals(expected, note)
    }

    @Test
    fun `test getLowestPitchClassOccurrenceBetweenBoundsOrNull a between 1 and 12 returns a-1`() {
        val lower = Note(midiNumber = 1)
        val upper = Note(midiNumber = 12)
        val note = MusicTheoryUtils.getLowestPitchClassOccurrenceBetweenBoundsOrNull(
            PitchClass.A,
            lower,
            upper,
        )

        val expected = Note(
            pitchClass = PitchClass.A,
            octave = -1,
        )
        assertEquals(expected, note)
    }

    @Test
    fun `test getLowestPitchClassOccurrenceBetweenBoundsOrNull a between 10 and 32 returns a0`() {
        val lower = Note(10)
        val upper = Note(32)
        val note = MusicTheoryUtils.getLowestPitchClassOccurrenceBetweenBoundsOrNull(
            PitchClass.A,
            lower,
            upper,
        )

        val expected = Note(
            PitchClass.A,
            octave = 0,
        )
        assertEquals(expected, note)
    }

    @Test
    fun `test getLowestPitchClassOccurrenceBetweenBoundsOrNull a between 10 and 20 returns null`() {
        val lower = Note(10)
        val upper = Note(20)
        val note = MusicTheoryUtils.getLowestPitchClassOccurrenceBetweenBoundsOrNull(
            PitchClass.A,
            lower,
            upper,
        )

        assertEquals(null, note)
    }

    @Test
    fun `test getNumberOfPitchClassOccurrencesBetweenBounds C between 0 and 0 returns 1`() {
        val lower = Note(0)
        val upper = Note(0)

        val actual = MusicTheoryUtils.getNumberOfPitchClassOccurrencesBetweenBounds(
            PitchClass.C,
            lower,
            upper,
        )

        assertEquals(1, actual)
    }

    @Test
    fun `test getNumberOfPitchClassOccurrencesBetweenBounds C between 0 and 1 returns 1`() {
        val lower = Note(0)
        val upper = Note(1)

        val actual = MusicTheoryUtils.getNumberOfPitchClassOccurrencesBetweenBounds(
            PitchClass.C,
            lower,
            upper,
        )

        assertEquals(1, actual)
    }

    @Test
    fun `test getNumberOfPitchClassOccurrencesBetweenBounds C between 0 and 12 returns 2`() {
        val lower = Note(0)
        val upper = Note(12)

        val actual = MusicTheoryUtils.getNumberOfPitchClassOccurrencesBetweenBounds(
            PitchClass.C,
            lower,
            upper,
        )

        assertEquals(2, actual)
    }

    @Test
    fun `test getNumberOfPitchClassOccurrencesBetweenBounds C between 1 and 23 returns 1`() {
        val lower = Note(1)
        val upper = Note(23)

        val actual = MusicTheoryUtils.getNumberOfPitchClassOccurrencesBetweenBounds(
            PitchClass.C,
            lower,
            upper,
        )

        assertEquals(1, actual)
    }

    @Test
    fun `test getNumberOfPitchClassOccurrencesBetweenBounds C between 1 and 11 returns 0`() {
        val lower = Note(1)
        val upper = Note(11)

        val actual = MusicTheoryUtils.getNumberOfPitchClassOccurrencesBetweenBounds(
            PitchClass.C,
            lower,
            upper,
        )

        assertEquals(0, actual)
    }

    @Test
    fun `test getNumberOfPitchClassOccurrencesBetweenBounds A between 0 and 12 returns 1`() {
        val lower = Note(0)
        val upper = Note(9)

        val actual = MusicTheoryUtils.getNumberOfPitchClassOccurrencesBetweenBounds(
            PitchClass.A,
            lower,
            upper,
        )

        assertEquals(1, actual)
    }

    @Test
    fun `test getNumberOfPitchClassOccurrencesBetweenBounds A between 0 and 0 returns 0`() {
        val lower = Note(0)
        val upper = Note(0)

        val actual = MusicTheoryUtils.getNumberOfPitchClassOccurrencesBetweenBounds(
            PitchClass.A,
            lower,
            upper,
        )

        assertEquals(0, actual)
    }

    @Test
    fun `test getNumberOfPitchClassOccurrencesBetweenBounds A between 9 and 21 returns 2`() {
        val lower = Note(9)
        val upper = Note(21)

        val actual = MusicTheoryUtils.getNumberOfPitchClassOccurrencesBetweenBounds(
            PitchClass.A,
            lower,
            upper,
        )

        assertEquals(2, actual)
    }

    @Test
    fun `test midiValueToNoteName 60 returns c4`() {
        val c4MidiNum = 60
        val noteName = MusicTheoryUtils.midiNumberToNoteName(c4MidiNum)
        assertEquals("C4", noteName)
    }

    @Test
    fun `test midiValueToNoteName 61 returns dFlat4 by default`() {
        val dFlat4MidiNum = 61
        val noteName = MusicTheoryUtils.midiNumberToNoteName(dFlat4MidiNum)
        assertEquals("D♭4", noteName)
    }

    @Test
    fun `test midiValueToNoteName 61 returns dFlat4 when asFlat is true`() {
        val dFlat4MidiNum = 61
        val noteName = MusicTheoryUtils.midiNumberToNoteName(dFlat4MidiNum, asFlat = true)
        assertEquals("D♭4", noteName)
    }

    @Test
    fun `test midiValueToNoteName 61 returns cSharp4 when asFlat is false`() {
        val cSharp4MidiNum = 61
        val noteName = MusicTheoryUtils.midiNumberToNoteName(cSharp4MidiNum, asFlat = false)
        assertEquals("C♯4", noteName)
    }

    @Test
    fun `test pitchClassOccursBetweenNoteBounds C occurs between notes 0 and 0`() {
        val occurs = MusicTheoryUtils.pitchClassOccursBetweenNoteBounds(
            pitchClass = PitchClass.C,
            lowerBound = Note(midiNumber = 0),
            upperBound = Note(midiNumber = 0),
        )
        assertTrue(occurs)
    }

    @Test
    fun `test pitchClassOccursBetweenNoteBounds dFlat does not occur between notes 0 and 0`() {
        val occurs = MusicTheoryUtils.pitchClassOccursBetweenNoteBounds(
            pitchClass = PitchClass.D_FLAT,
            lowerBound = Note(midiNumber = 0),
            upperBound = Note(midiNumber = 0),
        )
        assertFalse(occurs)
    }

    @Test
    fun `test pitchClassOccursBetweenNoteBounds C occurs between notes 12 and 12`() {
        val occurs = MusicTheoryUtils.pitchClassOccursBetweenNoteBounds(
            pitchClass = PitchClass.C,
            lowerBound = Note(midiNumber = 12),
            upperBound = Note(midiNumber = 12),
        )
        assertTrue(occurs)
    }

    @Test
    fun `test pitchClassOccursBetweenNoteBounds C occurs between notes 13 and 24`() {
        val occurs = MusicTheoryUtils.pitchClassOccursBetweenNoteBounds(
            pitchClass = PitchClass.C,
            lowerBound = Note(midiNumber = 13),
            upperBound = Note(midiNumber = 24),
        )
        assertTrue(occurs)
    }

    @Test
    fun `test pitchClassOccursBetweenNoteBounds C does not occur between notes 13 and 23`() {
        val occurs = MusicTheoryUtils.pitchClassOccursBetweenNoteBounds(
            pitchClass = PitchClass.C,
            lowerBound = Note(midiNumber = 13),
            upperBound = Note(midiNumber = 23),
        )
        assertFalse(occurs)
    }

    @Test
    fun `test pitchClassOccursBetweenNoteBounds A occurs between notes 13 and 23`() {
        val occurs = MusicTheoryUtils.pitchClassOccursBetweenNoteBounds(
            pitchClass = PitchClass.A,
            lowerBound = Note(midiNumber = 13),
            upperBound = Note(midiNumber = 23),
        )
        assertTrue(occurs)
    }

    @Test
    fun `test pitchClassOccursBetweenNoteBounds A occurs between notes 9 and 9`() {
        val occurs = MusicTheoryUtils.pitchClassOccursBetweenNoteBounds(
            pitchClass = PitchClass.A,
            lowerBound = Note(midiNumber = 9),
            upperBound = Note(midiNumber = 9),
        )
        assertTrue(occurs)
    }

    @Test
    fun `test pitchClassOccursBetweenNoteBounds A does not occur between notes 0 and 8`() {
        val occurs = MusicTheoryUtils.pitchClassOccursBetweenNoteBounds(
            pitchClass = PitchClass.A,
            lowerBound = Note(midiNumber = 0),
            upperBound = Note(midiNumber = 8),
        )
        assertFalse(occurs)
    }

    @Test
    fun `test pitchClassOccursBetweenNoteBounds A occurs between notes 10 and 12`() {
        val occurs = MusicTheoryUtils.pitchClassOccursBetweenNoteBounds(
            pitchClass = PitchClass.A,
            lowerBound = Note(midiNumber = 0),
            upperBound = Note(midiNumber = 8),
        )
        assertFalse(occurs)
    }

    @Test
    fun `test transformChromaticDegreesToIntervals with major scale sequence in key of C`() {
        val majorScaleChromaticDegrees = booleanArrayOf(
            true, false, true, false, true, true,
            false, true, false, true, false, true,
        )
        val keyOfC = 0
        val actual = MusicTheoryUtils.transformChromaticDegreesToIntervals(
            majorScaleChromaticDegrees,
            keyOfC,
        )

        val expected = intArrayOf(0, 2, 4, 5, 7, 9, 11)
        assertArrayEquals(expected, actual)
    }

    @Test
    fun `test transformChromaticDegreesToIntervals with major scale sequence in key of EFlat`() {
        val majorScaleChromaticDegrees = booleanArrayOf(
            true, false, true, false, true, true,
            false, true, false, true, false, true,
        )
        val keyOfEFlat = 3
        val actual = MusicTheoryUtils.transformChromaticDegreesToIntervals(
            majorScaleChromaticDegrees,
            keyOfEFlat,
        )

        // Notice that all intervals are (0 <= i <= 11)
        val expected = intArrayOf(0, 2, 3, 5, 7, 8, 10)
        assertArrayEquals(expected, actual)
    }

    @Test
    fun `test transformChromaticDegreesToIntervals with key of 12 throws IllegalArgumentException`() {
        val majorScaleChromaticDegrees = booleanArrayOf(
            true, false, true, false, true, true,
            false, true, false, true, false, true,
        )
        val erroneousKey = 12 // key must be (0 <= key <= 11)
        assertFailsWith<java.lang.IllegalArgumentException> {
            MusicTheoryUtils.transformChromaticDegreesToIntervals(majorScaleChromaticDegrees, erroneousKey)
        }
    }

    @Test
    fun `test transformChromaticDegreesToIntervals with degrees of size 11 throws IllegalArgumentException`() {
        // keys.size must equal 12
        val degreesOfSizeEleven = booleanArrayOf(
            true, false, true, false, true, true,
            false, true, false, true, false,
        )
        val keyOfEFlat = 3
        assertFailsWith<java.lang.IllegalArgumentException> {
            MusicTheoryUtils.transformChromaticDegreesToIntervals(degreesOfSizeEleven, keyOfEFlat)
        }
    }

    // TODO: refactor below here

    @Test
    fun `test ionian mode construction is correct`() {
        val expected = intArrayOf(0, 2, 4, 5, 7, 9, 11)

        val majorScaleSequence = MusicTheoryUtils.MAJOR_SCALE_INTERVALS
        val ionian = MusicTheoryUtils.getIntervalsForModeAtIx(majorScaleSequence, 0)

        assertArrayEquals(expected, ionian)
    }

    @Test
    fun `test dorian mode construction is correct`() {
        val expected = intArrayOf(0, 2, 3, 5, 7, 9, 10)
        val scale = MusicTheoryUtils.MAJOR_SCALE_INTERVALS
        assertArrayEquals(expected, MusicTheoryUtils.getIntervalsForModeAtIx(scale, 1))
    }

    @Test
    fun `test locrian mode construction is correct`() {
        val expected = intArrayOf(0, 1, 3, 5, 6, 8, 10)
        val scale = MusicTheoryUtils.MAJOR_SCALE_INTERVALS
        assertArrayEquals(expected, MusicTheoryUtils.getIntervalsForModeAtIx(scale, 6))
    }

    @Test
    fun `test melodic minor mode construction is correct`() {
        val expected = intArrayOf(0, 2, 3, 5, 7, 9, 11)
        val scale = MusicTheoryUtils.MELODIC_MINOR_SCALE_INTERVALS
        assertArrayEquals(expected, MusicTheoryUtils.getIntervalsForModeAtIx(scale, 0))
    }

    @Test
    fun `test altered mode construction is correct`() {
        val expected = intArrayOf(0, 1, 3, 4, 6, 8, 10)
        val scale = MusicTheoryUtils.MELODIC_MINOR_SCALE_INTERVALS
        assertArrayEquals(expected, MusicTheoryUtils.getIntervalsForModeAtIx(scale, 6))
    }

    @Test
    fun `test harmonic minor mode construction is correct`() {
        val expected = intArrayOf(0, 2, 3, 5, 7, 8, 11)
        val scale = MusicTheoryUtils.HARMONIC_MINOR_SCALE_INTERVALS
        assertArrayEquals(expected, MusicTheoryUtils.getIntervalsForModeAtIx(scale, 0))
    }

    @Test
    fun `test scale creation with non-zero starting index fails`() {
        val scale = intArrayOf(1, 4, 6)
        assertFailsWith<IllegalArgumentException> {
            MusicTheoryUtils.getIntervalsForModeAtIx(scale, 0)
        }
    }

    @Test
    fun `test index greater than intervals size fails`() {
        val scale = intArrayOf(0, 2, 4)
        assertFailsWith<ArrayIndexOutOfBoundsException> {
            MusicTheoryUtils.getIntervalsForModeAtIx(scale, 4)
        }
    }

    @Test
    fun `test index less than 0 throws exception`() {
        val scale = intArrayOf(0, 2, 4)
        assertFailsWith<ArrayIndexOutOfBoundsException> {
            MusicTheoryUtils.getIntervalsForModeAtIx(scale, -1)
        }
    }

    @Test
    fun `test transpose intervals 0`() {
        val cMaj = intArrayOf(0, 2, 4, 5, 7, 9, 11)
        val expected = intArrayOf(0, 2, 4, 5, 7, 9, 11) // C Major
        assertArrayEquals(expected, MusicTheoryUtils.transposeIntervals(cMaj, 0))
    }

    @Test
    fun `test transpose intervals 1`() {
        val cMaj = intArrayOf(0, 2, 4, 5, 7, 9, 11)
        val expected = intArrayOf(0, 1, 3, 5, 6, 8, 10) // Db Major
        assertArrayEquals(expected, MusicTheoryUtils.transposeIntervals(cMaj, 1))
    }

    @Test
    fun `test transpose intervals 2`() {
        val cMaj = intArrayOf(0, 2, 4, 5, 7, 9, 11)
        val expected = intArrayOf(1, 3, 4, 6, 8, 10, 11) // B Major
        assertArrayEquals(expected, MusicTheoryUtils.transposeIntervals(cMaj, 11))
    }

    @Test
    fun `test diatonic degrees 1`() {
        val seventh = booleanArrayOf(true, false, true, false, true, false, true) // 1 3 5 7
        val scale = MusicTheoryUtils.MAJOR_SCALE_INTERVALS
        val key = 0 // C
        val expected = intArrayOf(0, 4, 7, 11) // C E G B
        assertArrayEquals(expected, MusicTheoryUtils.transformDiatonicDegreesToIntervals(seventh, scale, key))
    }

    @Test
    fun `test diatonic degrees 2`() {
        val seventh = booleanArrayOf(true, false, true, false, true, false, true) // 1 3 5 7
        val scale = MusicTheoryUtils.MAJOR_SCALE_INTERVALS
        val key = 3 // Eb
        val expected = intArrayOf(2, 3, 7, 10) // D Eb G Bb
        assertArrayEquals(expected, MusicTheoryUtils.transformDiatonicDegreesToIntervals(seventh, scale, key))
    }
}
