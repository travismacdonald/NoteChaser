package com.cannonballapps.notechaser.playablegenerator


import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import org.junit.Assert.*
import org.junit.Test

class PlayableTest {

    @Test
    fun `test pattern notes is correct`() {
        val template = PatternTemplate(arrayListOf(0, 4, 7))
        val pattern = Playable(template, 12)
        assertEquals(arrayListOf(Note(12), Note(16), Note(19)), pattern.notes)
    }

    @Test
    fun `test pattern notes is correct when template min is greater than 0`() {
        val template = PatternTemplate(arrayListOf(1, 5, 8))
        val pattern = Playable(template, 12)
        assertEquals(arrayListOf(Note(12), Note(16), Note(19)), pattern.notes)
    }

    @Test
    fun `test pattern size is correct`() {
        val template = PatternTemplate(arrayListOf(5, 1, 8))
        val pattern = Playable(template, 12)
        assertEquals(3, pattern.size)
    }

//    @Test
//    fun `test pattern constructed with empty template throws exception`() {
//        val template = PatternTemplate(arrayListOf())
//        assertFailsWith<EmptyTemplateException> { val pattern = Playable(template, 4) }
//    }

    @Test
    fun `test equals is true with same notes`() {
        val template = PatternTemplate(arrayListOf(3, 2, 4))
        val lhs = Playable(template, 5)
        val rhs = Playable(template, 5)
        assertTrue(lhs == rhs)
    }

    @Test
    fun `test equals is false with different notes`() {
        val template = PatternTemplate(arrayListOf(3, 2, 4))
        val lhs = Playable(template, 5)
        val rhs = Playable(template, 3)
        assertTrue(lhs != rhs)
    }

    @Test
    fun `test toString works correctly`() {
        val template = PatternTemplate(arrayListOf(0, 4, 7))
        val pattern = Playable(template, 13)
        assertEquals("13 17 20", pattern.toString())
    }

    @Test
    fun `test toStringFlat works correctly`() {
        val template = PatternTemplate(arrayListOf(0, 4, 7))
        val pattern = Playable(template, 13)
        assertEquals(
                "D${MusicTheoryUtils.FLAT} F A${MusicTheoryUtils.FLAT}",
                pattern.toStringFlat()
        )
    }

    @Test
    fun `test toStringSharp works correctly`() {
        val template = PatternTemplate(arrayListOf(0, 4, 7))
        val pattern = Playable(template, 13)
        assertEquals(
                "C${MusicTheoryUtils.SHARP} F G${MusicTheoryUtils.SHARP}", // perhaps E# would be more correct
                pattern.toStringSharp()
        )
    }

}