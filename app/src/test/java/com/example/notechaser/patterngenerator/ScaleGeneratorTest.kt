package com.example.notechaser.patterngenerator

import org.junit.Assert.*
import org.junit.Test

class ScaleGeneratorTest {

    @Test
    fun testIonian() {
        val template = ScaleGenerator.generateMajorMode(0)
        val ans = arrayListOf(0, 2, 4, 5, 7, 9, 11)
        assertEquals(ans, template.intervalsTransposed)
    }

    @Test
    fun testDorian() {
        val template = ScaleGenerator.generateMajorMode(1)
        val ans = arrayListOf(0, 2, 3, 5, 7, 9, 10)
        assertEquals(ans, template.intervalsTransposed)
    }

    @Test
    fun testLocrian() {
        val template = ScaleGenerator.generateMajorMode(6)
        val ans = arrayListOf(0, 1, 3, 5, 6, 8, 10)
        assertEquals(ans, template.intervalsTransposed)
    }

    @Test
    fun testMelodicMinor() {
        val template = ScaleGenerator.generateMelodicMinorMode(0)
        val ans = arrayListOf(0, 2, 3, 5, 7, 9, 11)
        assertEquals(ans, template.intervalsTransposed)
    }

    @Test
    fun testAltered() {
        val template = ScaleGenerator.generateMelodicMinorMode(6)
        val ans = arrayListOf(0, 1, 3, 4, 6, 8, 10)
        assertEquals(ans, template.intervalsTransposed)
    }

    @Test
    fun testHarmonicMinor() {
        val template = ScaleGenerator.generateHarmonicMinorMode(0)
        val ans = arrayListOf(0, 2, 3, 5, 7, 8, 11)
        assertEquals(ans, template.intervalsTransposed)
    }

}