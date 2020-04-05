package com.example.notechaser.patterngenerator

import org.junit.Assert.*
import org.junit.Test

class NoteTest {

    val SHARP = '\u266F'
    val FLAT = '\u266d'
    val NATURAL = '\u266e'

    @Test
    fun `test notes equal passes with same index`() {
        val lhs = Note(0)
        val rhs = Note(0)
        assertTrue(lhs == rhs)
    }

    @Test
    fun `test equals fails with different indexes`() {
        val lhs = Note(0)
        val rhs = Note(1)
        assertTrue(lhs != rhs)
    }

    @Test
        fun `test class stores index correctly`() {
        val note = Note(0)
        assertEquals(0, note.ix)
    }

    @Test
    fun `test nameSharp is correct for index 0`() {
        val c = Note(0)
        assertEquals("C", c.nameSharp)
    }

    @Test
    fun `test nameFlat is correct for index 0`() {
        val c = Note(0)
        assertEquals("C", c.nameFlat)
    }

    @Test
    fun `test note constructs when index is greater than 12`() {
        val c = Note(12)
        assertEquals(12, c.ix)
    }

    @Test
    fun `test nameSharp is correct when given index greater than 12`() {
        val c = Note(12)
        assertEquals("C", c.nameSharp)
    }

    @Test
    fun `test nameFlat is correct when given index greater than 12`() {
        val c = Note(12)
        assertEquals("C", c.nameFlat)
    }

    @Test
    fun `test nameSharp is correct when index equals 1`() {
        val cSharp = Note(1)
        assertEquals("C$SHARP", cSharp.nameSharp)
    }

    @Test
    fun `test nameFlat is correct when index equals 1`() {
        val dFlat = Note(1)
        assertEquals("D$FLAT", dFlat.nameFlat)
    }

}