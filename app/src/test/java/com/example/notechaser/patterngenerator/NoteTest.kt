package com.example.notechaser.patterngenerator

import org.junit.Assert.*
import org.junit.Test

class NoteTest {

    @Test
    fun note_c0() {
        val c = Note(0)
        assertEquals(c, Note(0))
    }

}