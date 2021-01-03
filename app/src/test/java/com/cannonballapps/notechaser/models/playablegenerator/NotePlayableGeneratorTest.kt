package com.cannonballapps.notechaser.models.playablegenerator

import com.cannonballapps.notechaser.musicutilities.*
import junit.framework.TestCase
import org.junit.Test


class NotePlayableGeneratorTest : TestCase() {

    val rootOnlyDiatonicDegree = booleanArrayOf(
            true, false, false, false, false, false, false
    )

    val triadDiatonicDegrees = booleanArrayOf(
            true, false, true, false, true, false, false,
    )

    val majorChromaticDegrees = booleanArrayOf(
            true, false, true, false, true, true,
            false, true, false, true, false, true
    )

    @Test
    fun `test makeNotePlayableGeneratorFromDiatonicDegrees generates notes between bounds`() {
        val scale = ParentScale2.MAJOR.getModeAtIx(0)
        val lower = NoteFactory.makeNoteFromMidiNumber(36)
        val upper = NoteFactory.makeNoteFromMidiNumber(48)

        val generator = PlayableGeneratorFactory.makeNotePlayableGeneratorFromDiatonicDegrees(
                rootOnlyDiatonicDegree,
                scale,
                PitchClass.C,
                lower,
                upper
        )
        for (i in 0 until 100) {
            val curPlayable = generator.generatePlayable()
            assertTrue(curPlayable.notes.size == 1)
            assertTrue(curPlayable.notes[0].midiNumber >= lower.midiNumber &&
                    curPlayable.notes[0].midiNumber <= upper.midiNumber)
        }
    }

    @Test
    fun `test makeNotePlayableGeneratorFromDiatonicDegrees generates notes between bounds 2`() {
        val scale = ParentScale2.MAJOR.getModeAtIx(0)
        val lower = NoteFactory.makeNoteFromMidiNumber(36)
        val upper = NoteFactory.makeNoteFromMidiNumber(48)

        val generator = PlayableGeneratorFactory.makeNotePlayableGeneratorFromDiatonicDegrees(
                rootOnlyDiatonicDegree,
                scale,
                PitchClass.C,
                lower,
                upper
        )
        for (i in 0 until 100) {
            val curPlayable = generator.generatePlayable()
            assertTrue(curPlayable.notes.size == 1)
            assertTrue(curPlayable.notes[0].midiNumber >= lower.midiNumber &&
                    curPlayable.notes[0].midiNumber <= upper.midiNumber)
        }
    }


}