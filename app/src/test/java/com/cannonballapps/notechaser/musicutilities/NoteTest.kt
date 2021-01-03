package com.cannonballapps.notechaser.musicutilities

import junit.framework.TestCase
import org.junit.Test

class NoteTest : TestCase() {

    @Test
    fun `test toString for c0`() {
        val note = NoteFactory.makeNoteFromPitchClassAndOctave(PitchClass.C, 0)
        assertEquals("C0", note.toString())
    }

    @Test
    fun `test toString for c4`() {
        val note = NoteFactory.makeNoteFromPitchClassAndOctave(PitchClass.C, 4)
        assertEquals("C4", note.toString())
    }

    @Test
    fun `test toString for cSharp4`() {
        val note = NoteFactory.makeNoteFromPitchClassAndOctave(PitchClass.C_SHARP, 4)
        assertEquals("C♯4", note.toString())
    }

    @Test
    fun `test toString for dFlat4`() {
        val note = NoteFactory.makeNoteFromPitchClassAndOctave(PitchClass.D_FLAT, 4)
        assertEquals("D♭4", note.toString())
    }

}