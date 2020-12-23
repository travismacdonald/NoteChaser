package com.cannonballapps.notechaser.playablegenerator

import com.cannonballapps.notechaser.playablegenerator.exceptions.EmptyTemplateException
import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

class PatternTemplateTest {

    @Test
    fun `test intervals should not transpose`() {
        val template = PatternTemplate(arrayListOf(0, 2, 4))
        assertEquals(arrayListOf(0, 2, 4), template.intervalsTransposed)
    }

    @Test
    fun `test intervals should transpose down`() {
        val template = PatternTemplate(arrayListOf(1, 3, 5))
        assertEquals(arrayListOf(0, 2, 4), template.intervalsTransposed)
    }

    @Test
    fun `test intervals should transpose up`() {
        val template = PatternTemplate(arrayListOf(-1, 1, 3))
        assertEquals(arrayListOf(0, 2, 4), template.intervalsTransposed)
    }

    @Test
    fun `test intervals should not transpose based on middle interval`() {
        val template = PatternTemplate(arrayListOf(2, 0, 4))
        assertEquals(arrayListOf(2, 0, 4), template.intervalsTransposed)
    }

    @Test
    fun `test intervals should transpose down based on middle interval`() {
        val template = PatternTemplate(arrayListOf(3, 1, 5))
        assertEquals(arrayListOf(2, 0, 4), template.intervalsTransposed)
    }

    @Test
    fun `test intervals should transpose up based on middle interval`() {
        val template = PatternTemplate(arrayListOf(1, -1, 3))
        assertEquals(arrayListOf(2, 0, 4), template.intervalsTransposed)
    }

    @Test
    fun `test intervals backing property`() {
        val template = PatternTemplate(arrayListOf(2, 4, 6))
        assertEquals(arrayListOf(2, 4, 6), template.intervals)
    }

    @Test
    fun `test intervals transpose correctly after addInterval`() {
        val template = PatternTemplate(arrayListOf(3, 5, 7))
        // templates.intervalsTransposed == [ 0, 2, 4 ]
        template.addInterval(1)
        assertEquals(arrayListOf(2, 4, 6, 0), template.intervalsTransposed)
    }

    @Test
    fun `test template has correct range with min == 0`() {
        val template = PatternTemplate(arrayListOf(0, 2, 4))
        assertEquals(4, template.range)
    }

    @Test
    fun `test template has correct range with min greater than 0`() {
        val template = PatternTemplate(arrayListOf(1, 3, 5))
        assertEquals(4, template.range)
    }

    @Test
    fun `test template has correct range with min less than 0`() {
        val template = PatternTemplate(arrayListOf(-1, 1, 3))
        assertEquals(4, template.range)
    }

    @Test
    fun `test range throws exception with empty template`() {
        val template = PatternTemplate()
        assertFailsWith<EmptyTemplateException> { template.range }

    }

    @Test
    fun `test range with only one interval in template`() {
        val template = PatternTemplate(arrayListOf(1))
        assertEquals(0, template.range)

    }

    @Test
    fun `test range is correct after addInterval`() {
        val template = PatternTemplate()
        template.addInterval(4)
        template.addInterval(10)
        assertEquals(6, template.range)
    }

    @Test
    fun `test range is correct after removeIntervalAt`() {
        val template = PatternTemplate(arrayListOf(4))
        template.removeIntervalAt(0)
        assertFailsWith<EmptyTemplateException> { template.range }
    }

    @Test
    fun `test addInterval works correctly`() {
        val template = PatternTemplate(arrayListOf(3, 5, 7))
        template.addInterval(1)
        assertEquals(arrayListOf(3, 5, 7, 1), template.intervals)
    }

    @Test
    fun `test addAllIntervals works correctly`() {
        val template = PatternTemplate()
        template.addAllIntervals(0, 4, 7)
        assertEquals(arrayListOf(0, 4, 7), template.intervalsTransposed)
    }

    @Test
    fun `test intervalsTransposed is correct after removeIntervalAt`() {
        val template = PatternTemplate(arrayListOf(3, 5, 7))
        // templates.intervalsTransposed == [ 0, 2, 4 ]
        template.removeIntervalAt(0)
        assertEquals(arrayListOf(0, 2), template.intervalsTransposed)
    }

    @Test
    fun `test intervals correct after removeIntervalAt`() {
        val template = PatternTemplate(arrayListOf(3, 5, 7))
        template.removeIntervalAt(0)
        assertEquals(arrayListOf(5, 7), template.intervals)
    }

    @Test
    fun `test equals passes with same intervals`() {
        val lhs = PatternTemplate(arrayListOf(3, 5, 7))
        val rhs = PatternTemplate(arrayListOf(3, 5, 7))
        assertTrue(rhs == lhs)
    }

    @Test
    fun `test equals true with different intervals but same intervalsTransposed`() {
        val lhs = PatternTemplate(arrayListOf(3, 5, 7))
        val rhs = PatternTemplate(arrayListOf(2, 4, 6))
        assertTrue(lhs == rhs)
    }

    @Test
    fun `test intervalsTransposed is same with different intervals`() {
        val lhs = PatternTemplate(arrayListOf(3, 5, 7))
        val rhs = PatternTemplate(arrayListOf(2, 4, 6))
        assertTrue(lhs.intervalsTransposed == rhs.intervalsTransposed)
    }

    @Test
    fun `test toString`() {
        val template = PatternTemplate(arrayListOf(1, 5, 8))
        assertEquals("1 5 8", template.toString())
    }

}