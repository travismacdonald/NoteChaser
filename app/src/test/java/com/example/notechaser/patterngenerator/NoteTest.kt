package com.example.notechaser.patterngenerator

import org.junit.Assert.*
import org.junit.Test

class NoteTest {

    val SHARP = '\u266F'
    val FLAT = '\u266d'
    val NATURAL = '\u266e'

    @Test
    fun c0_equals() {
        val c = Note(0)
        assertEquals(Note(0), c)
    }

    @Test
    fun c0_c12_notEqual() {
        val c0 = Note(0)
        val c1 = Note(1)
        assertNotEquals(c0, c1)
    }

    @Test
    fun c0_ix() {
        val c = Note(0)
        assertEquals(0, c.ix)
    }

    @Test
    fun c0_nameSharp() {
        val c = Note(0)
        assertEquals("C", c.nameSharp)
    }

    @Test
    fun c0_nameFlat() {
        val c = Note(0)
        assertEquals("C", c.nameFlat)
    }

    @Test
    fun c12_ix() {
        val c = Note(12)
        assertEquals(12, c.ix)
    }

    @Test
    fun c12_nameSharp() {
        val c = Note(12)
        assertEquals("C", c.nameSharp)
    }

    @Test
    fun c12_nameFlat() {
        val c = Note(12)
        assertEquals("C", c.nameFlat)
    }

    @Test
    fun dFlat0_ix() {
        val dFlat = Note(1)
        assertEquals(1, dFlat.ix)
    }

    @Test
    fun dFlat0_nameSharp() {
        val dFlat = Note(1)
        assertEquals("C$SHARP", dFlat.nameSharp)
    }

    @Test
    fun dFlat0_nameFlat() {
        val dFlat = Note(1)
        assertEquals("D$FLAT", dFlat.nameFlat)
    }

}