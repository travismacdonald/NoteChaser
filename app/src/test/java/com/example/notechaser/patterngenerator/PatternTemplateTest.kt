package com.example.notechaser.patterngenerator

import org.junit.Assert.*
import org.junit.Test

class PatternTemplateTest {

    @Test
    fun testIntervalsTransposedNone() {
        val template = PatternTemplate(arrayListOf(0, 2, 4))
        assertEquals(arrayListOf(0, 2, 4), template.intervalsTransposed)
    }

    @Test
    fun testIntervalsTransposedDown() {
        val template = PatternTemplate(arrayListOf(1, 3, 5))
        assertEquals(arrayListOf(0, 2, 4), template.intervalsTransposed)
    }

    @Test
    fun testIntervalsTransposedUp() {
        val template = PatternTemplate(arrayListOf(-1, 1, 3))
        assertEquals(arrayListOf(0, 2, 4), template.intervalsTransposed)
    }

    @Test
    fun testIntervalsTransposedNoneRange() {
        val template = PatternTemplate(arrayListOf(0, 2, 4))
        assertEquals(4, template.range)
    }

    @Test
    fun testIntervalsTransposedDownRange() {
        val template = PatternTemplate(arrayListOf(1, 3, 5))
        assertEquals(4, template.range)
    }

    @Test
    fun testIntervalsTransposedUpRange() {
        val template = PatternTemplate(arrayListOf(-1, 1, 3))
        assertEquals(4, template.range)
    }

    @Test
    fun testIntervalsTransposedNoneMid() {
        val template = PatternTemplate(arrayListOf(2, 0, 4))
        assertEquals(arrayListOf(2, 0, 4), template.intervalsTransposed)
    }

    @Test
    fun testIntervalsTransposedDownMid() {
        val template = PatternTemplate(arrayListOf(3, 1, 5))
        assertEquals(arrayListOf(2, 0, 4), template.intervalsTransposed)
    }

    @Test
    fun testIntervalsTransposedUpMid() {
        val template = PatternTemplate(arrayListOf(1, -1, 3))
        assertEquals(arrayListOf(2, 0, 4), template.intervalsTransposed)
    }

    @Test
    fun testIntervalsUntransposed() {
        val template = PatternTemplate(arrayListOf(2, 4, 6))
        assertEquals(arrayListOf(2, 4, 6), template.intervalsUntransposed)
    }

    @Test
    fun testAddIntervalTransposeDown() {
        val template = PatternTemplate(arrayListOf(3, 5, 7))
        // templates.intervalsTransposed == [ 0, 2, 4 ]
        template.addInterval(1)
        assertEquals(arrayListOf(2, 4, 6, 0), template.intervalsTransposed)
    }

    @Test
    fun testAddIntervalUntransposedDown() {
        val template = PatternTemplate(arrayListOf(3, 5, 7))
        template.addInterval(1)
        assertEquals(arrayListOf(3, 5, 7, 1), template.intervalsUntransposed)
    }

    @Test
    fun testRemoveIntervalTransposeDown() {
        val template = PatternTemplate(arrayListOf(3, 5, 7))
        // templates.intervalsTransposed == [ 0, 2, 4 ]
        template.removeIntervalAt(0)
        assertEquals(arrayListOf(0, 2), template.intervalsTransposed)
    }

    @Test
    fun testRemoveIntervalNoTransposeDown() {
        val template = PatternTemplate(arrayListOf(3, 5, 7))
        template.removeIntervalAt(0)
        assertEquals(arrayListOf(5, 7), template.intervalsUntransposed)
    }

}