package com.example.notechaser.playablegenerator

import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

class ScaleGeneratorTest {

    @Test
    fun `test ionian mode construction is correct`() {
        val template = ScaleGenerator.generateMajorMode(0)
        val ans = arrayListOf(0, 2, 4, 5, 7, 9, 11)
        assertEquals(ans, template.intervalsTransposed)
    }

    @Test
    fun `test dorian mode construction is correct`() {
        val template = ScaleGenerator.generateMajorMode(1)
        val ans = arrayListOf(0, 2, 3, 5, 7, 9, 10)
        assertEquals(ans, template.intervalsTransposed)
    }

    @Test
    fun `test locrian mode construction is correct`() {
        val template = ScaleGenerator.generateMajorMode(6)
        val ans = arrayListOf(0, 1, 3, 5, 6, 8, 10)
        assertEquals(ans, template.intervalsTransposed)
    }

    @Test
    fun `test melodic minor mode construction is correct`() {
        val template = ScaleGenerator.generateMelodicMinorMode(0)
        val ans = arrayListOf(0, 2, 3, 5, 7, 9, 11)
        assertEquals(ans, template.intervalsTransposed)
    }

    @Test
    fun `test altered mode construction is correct`() {
        val template = ScaleGenerator.generateMelodicMinorMode(6)
        val ans = arrayListOf(0, 1, 3, 4, 6, 8, 10)
        assertEquals(ans, template.intervalsTransposed)
    }

    @Test
    fun `test harmonic minor mode construction is correct`() {
        val template = ScaleGenerator.generateHarmonicMinorMode(0)
        val ans = arrayListOf(0, 2, 3, 5, 7, 8, 11)
        assertEquals(ans, template.intervalsTransposed)
    }

    @Test
    fun `test index greater than 6 throws exception`() {
        assertFailsWith<ArrayIndexOutOfBoundsException> {
            val template = ScaleGenerator.generateMajorMode(7)
        }
    }

    @Test
    fun `test index less than 0 throws exception`() {
        assertFailsWith<ArrayIndexOutOfBoundsException> {
            val template = ScaleGenerator.generateMajorMode(-1)
        }
    }

}