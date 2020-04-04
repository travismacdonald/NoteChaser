package com.example.notechaser.patterngenerator

import org.junit.Assert.*
import org.junit.Test

class PatternTest {

    @Test
    fun testPatternInit() {
        val template = PatternTemplate(arrayListOf(0, 4, 7))
        val pattern = Pattern(template, 12)
        assertEquals(arrayListOf(Note(12), Note(16), Note(19)), pattern.notes)
    }

    @Test
    fun testPatternInit2() {
        val template = PatternTemplate(arrayListOf(1, 5, 8))
        val pattern = Pattern(template, 12)
        assertEquals(arrayListOf(Note(12), Note(16), Note(19)), pattern.notes)
    }

    @Test
    fun testPatternInit3() {
        val template = PatternTemplate(arrayListOf(5, 1, 8))
        val pattern = Pattern(template, 12)
        assertEquals(arrayListOf(Note(16), Note(12), Note(19)), pattern.notes)
    }

    @Test
    fun testPatternInitSize() {
        val template = PatternTemplate(arrayListOf(5, 1, 8))
        val pattern = Pattern(template, 12)
        assertEquals(3, pattern.size)
    }


}