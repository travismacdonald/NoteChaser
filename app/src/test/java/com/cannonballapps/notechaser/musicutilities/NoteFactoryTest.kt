package com.cannonballapps.notechaser.musicutilities

import junit.framework.TestCase
import org.junit.Test
import kotlin.test.assertFailsWith

class NoteFactoryTest : TestCase() {

    @Test
    fun `test getNoteInstanceFromMidiNumber 60 makes c4`() {
        val note = NoteFactory.makeNoteFromMidiNumber(60)
        kotlin.test.assertEquals(PitchClass.C, note.pitchClass)
        kotlin.test.assertEquals(4, note.octave)
    }

    @Test
    fun `test getNoteInstanceFromMidiNumber 12 makes c0`() {
        val note = NoteFactory.makeNoteFromMidiNumber(12)
        kotlin.test.assertEquals(PitchClass.C, note.pitchClass)
        kotlin.test.assertEquals(0, note.octave)
    }

    @Test
    fun `test getNoteInstanceFromMidiNumber 59 makes b3`() {
        val note = NoteFactory.makeNoteFromMidiNumber(59)
        kotlin.test.assertEquals(PitchClass.B, note.pitchClass)
        kotlin.test.assertEquals(3, note.octave)
    }

    @Test
    fun `test getNoteInstanceFromMidiNumber 0 makes c-1`() {
        val note = NoteFactory.makeNoteFromMidiNumber(0)
        kotlin.test.assertEquals(PitchClass.C, note.pitchClass)
        kotlin.test.assertEquals(-1, note.octave)
    }

    @Test
    fun `test getNoteInstanceFromMidiNumber -1 throws exception`() {
        assertFailsWith<IllegalArgumentException> {
            NoteFactory.makeNoteFromMidiNumber(-1)
        }
    }

    @Test
    fun `test getNoteInstanceFromMidiNumber 109 throws exception`() {
        assertFailsWith<IllegalArgumentException> {
            NoteFactory.makeNoteFromMidiNumber(109)
        }
    }

    @Test
    fun `test getNoteInstanceFromPitchClassAndOctave c4 makes 60`() {
        val note = NoteFactory.makeNoteFromPitchClassAndOctave(
                PitchClass.C,
                4
        )
        kotlin.test.assertEquals(60, note.midiNumber)
    }

    @Test
    fun `test getNoteInstanceFromPitchClassAndOctave c0 makes 12`() {
        val note = NoteFactory.makeNoteFromPitchClassAndOctave(PitchClass.C, 0)
        kotlin.test.assertEquals(12, note.midiNumber)
    }

    @Test
    fun `test getNoteInstanceFromPitchClassAndOctave b3 makes 59`() {
        val note = NoteFactory.makeNoteFromPitchClassAndOctave(
                PitchClass.B,
                3
        )
        kotlin.test.assertEquals(59, note.midiNumber)
    }

    @Test
    fun `test getNoteInstanceFromPitchClassAndOctave c-1 makes 0`() {
        val note = NoteFactory.makeNoteFromPitchClassAndOctave(PitchClass.C, -1)
        kotlin.test.assertEquals(0, note.midiNumber)
    }

    @Test
    fun `test getNoteInstanceFromPitchClassAndOctave c-2 throws exception`() {
        assertFailsWith<IllegalArgumentException> {
            NoteFactory.makeNoteFromPitchClassAndOctave(PitchClass.C, -2)
        }
    }

    @Test
    fun `test getNoteInstanceFromPitchClassAndOctave cSharp8 throws exception`() {
        assertFailsWith<IllegalArgumentException> {
            NoteFactory.makeNoteFromPitchClassAndOctave(PitchClass.C_SHARP, 8)
        }
    }

}