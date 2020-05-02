package com.example.notechaser.utilities

import org.junit.Assert.assertArrayEquals
import org.junit.Test
import kotlin.test.assertFailsWith

class MusicTheoryUtilsTest {

    @Test
    fun `test ionian mode construction is correct`() {
        val expected = intArrayOf(0, 2, 4, 5, 7, 9, 11)
        val scale = MusicTheoryUtils.MAJOR_SCALE_SEQUENCE
        assertArrayEquals(expected, MusicTheoryUtils.getModeIntervals(scale, 0))
    }

    @Test
    fun `test dorian mode construction is correct`() {
        val expected = intArrayOf(0, 2, 3, 5, 7, 9, 10)
        val scale = MusicTheoryUtils.MAJOR_SCALE_SEQUENCE
        assertArrayEquals(expected, MusicTheoryUtils.getModeIntervals(scale, 1))
    }

    @Test
    fun `test locrian mode construction is correct`() {
        val expected = intArrayOf(0, 1, 3, 5, 6, 8, 10)
        val scale = MusicTheoryUtils.MAJOR_SCALE_SEQUENCE
        assertArrayEquals(expected, MusicTheoryUtils.getModeIntervals(scale, 6))
    }

    @Test
    fun `test melodic minor mode construction is correct`() {
        val expected = intArrayOf(0, 2, 3, 5, 7, 9, 11)
        val scale = MusicTheoryUtils.MELODIC_MINOR_SCALE_SEQUENCE
        assertArrayEquals(expected, MusicTheoryUtils.getModeIntervals(scale, 0))
    }

    @Test
    fun `test altered mode construction is correct`() {
        val expected = intArrayOf(0, 1, 3, 4, 6, 8, 10)
        val scale = MusicTheoryUtils.MELODIC_MINOR_SCALE_SEQUENCE
        assertArrayEquals(expected, MusicTheoryUtils.getModeIntervals(scale, 6))
    }

    @Test
    fun `test harmonic minor mode construction is correct`() {
        val expected = intArrayOf(0, 2, 3, 5, 7, 8, 11)
        val scale = MusicTheoryUtils.HARMONIC_MINOR_SCALE_SEQUENCE
        assertArrayEquals(expected, MusicTheoryUtils.getModeIntervals(scale, 0))
    }

    @Test
    fun `test scale creation with non-zero starting index fails`() {
        val scale = intArrayOf(1, 4, 6)
        assertFailsWith<IllegalArgumentException> {
            MusicTheoryUtils.getModeIntervals(scale, 0)
        }
    }

    @Test
    fun `test index greater than intervals size fails`() {
        val scale = intArrayOf(0, 2, 4)
        assertFailsWith<ArrayIndexOutOfBoundsException> {
            MusicTheoryUtils.getModeIntervals(scale, 4)
        }
    }

    @Test
    fun `test index less than 0 throws exception`() {
        val scale = intArrayOf(0, 2, 4)
        assertFailsWith<ArrayIndexOutOfBoundsException> {
            MusicTheoryUtils.getModeIntervals(scale, -1)
        }
    }

    @Test
    fun `test chromatic degrees transformation C major`() {
        // C D E F G A B
        val degrees = booleanArrayOf(
                true, false, true, false, true, true,
                false, true, false, true, false, true
        )
        // C major
        val key = 0
        val expected = intArrayOf(0, 2, 4, 5, 7, 9, 11)
        assertArrayEquals(expected, MusicTheoryUtils.transformChromaticDegreesToIntervals(degrees, key))
    }

    @Test
    fun `test chromatic degrees transformation Eb major`() {
        // C D E F G A B
        val degrees = booleanArrayOf(
                true, false, true, false, true, true,
                false, true, false, true, false, true
        )
        // C major
        val key = 3
        val expected = intArrayOf(0, 2, 3, 5, 7, 8, 10)
        assertArrayEquals(expected, MusicTheoryUtils.transformChromaticDegreesToIntervals(degrees, key))
    }

    @Test
    fun `test chromatic degrees transformation throws error with illegal key`() {
        // C D E F G A B
        val degrees = booleanArrayOf(
                true, false, true, false, true, true,
                false, true, false, true, false, true
        )
        // C major
        val key = 12 // key must be less than or equal to 11
        assertFailsWith<java.lang.IllegalArgumentException> {
            MusicTheoryUtils.transformChromaticDegreesToIntervals(degrees, key)
        }
    }

    @Test
    fun `test chromatic degrees transformation throws error with illegal sized chromatic degrees`() {
        // Must be size 12
        val degrees = booleanArrayOf(
                true, false, true, false, true, true,
                false, true, false, true, false
        )
        // C major
        val key = 3
        assertFailsWith<java.lang.IllegalArgumentException> {
            MusicTheoryUtils.transformChromaticDegreesToIntervals(degrees, key)
        }
    }

    @Test
    fun `test transpose intervals 0`() {
        val cMaj = intArrayOf( 0, 2, 4, 5, 7, 9, 11 )
        val expected = intArrayOf( 0, 2, 4, 5, 7, 9, 11 ) // C Major
        assertArrayEquals(expected, MusicTheoryUtils.transposeIntervals(cMaj, 0))
    }

    @Test
    fun `test transpose intervals 1`() {
        val cMaj = intArrayOf( 0, 2, 4, 5, 7, 9, 11 )
        val expected = intArrayOf( 0, 1, 3, 5, 6, 8, 10 ) // Db Major
        assertArrayEquals(expected, MusicTheoryUtils.transposeIntervals(cMaj, 1))
    }

    @Test
    fun `test transpose intervals 2`() {
        val cMaj = intArrayOf( 0, 2, 4, 5, 7, 9, 11 )
        val expected = intArrayOf( 1, 3, 4, 6, 8, 10, 11 ) // B Major
        assertArrayEquals(expected, MusicTheoryUtils.transposeIntervals(cMaj, 11))
    }

    @Test
    fun `test diatonic degrees 1`() {
        val seventh = booleanArrayOf(true, false, true, false, true, false, true) // 1 3 5 7
        val scale = MusicTheoryUtils.MAJOR_SCALE_SEQUENCE
        val key = 0 // C
        val expected = intArrayOf(0, 4, 7, 11) // C E G B
        assertArrayEquals(expected, MusicTheoryUtils.transformDiatonicDegreesToIntervals(seventh, scale, key))
    }

    @Test
    fun `test diatonic degrees 2`() {
        val seventh = booleanArrayOf(true, false, true, false, true, false, true) // 1 3 5 7
        val scale = MusicTheoryUtils.MAJOR_SCALE_SEQUENCE
        val key = 3 // Eb
        val expected = intArrayOf(2, 3, 7, 10) // D Eb G Bb
        assertArrayEquals(expected, MusicTheoryUtils.transformDiatonicDegreesToIntervals(seventh, scale, key))
    }

}