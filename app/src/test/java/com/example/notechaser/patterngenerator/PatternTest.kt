package com.example.notechaser.patterngenerator

import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

class PatternTest {

    @Test
    fun `test pattern notes is correct`() {
        val template = PatternTemplate(arrayListOf(0, 4, 7))
        val pattern = Pattern(template, 12)
        assertEquals(arrayListOf(Note(12), Note(16), Note(19)), pattern.notes)
    }

    @Test
    fun `test pattern notes is correct when template min is greater than 0`() {
        val template = PatternTemplate(arrayListOf(1, 5, 8))
        val pattern = Pattern(template, 12)
        assertEquals(arrayListOf(Note(12), Note(16), Note(19)), pattern.notes)
    }

    @Test
    fun `test pattern size is correct`() {
        val template = PatternTemplate(arrayListOf(5, 1, 8))
        val pattern = Pattern(template, 12)
        assertEquals(3, pattern.size)
    }

    @Test
    fun `test pattern constructed with empty template throws exception`() {
        val template = PatternTemplate(arrayListOf())
        assertFailsWith<EmptyPatternTemplateException> { val pattern = Pattern(template, 4) }
    }

}