package com.cannonballapps.notechaser.playablegenerator

import com.cannonballapps.notechaser.musicutilities.ChordTemplate
import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

class ChordTemplateTest {

    @Test
    fun `test maintain init list`() {
        val template = ChordTemplate(arrayListOf(0, 2, 4))
        assertEquals(listOf(0, 2, 4), template.intervals)
    }

    @Test
    fun `test sort init list`() {
        val template = ChordTemplate(arrayListOf(2, 0, 4))
        assertEquals(listOf(0, 2, 4), template.intervals)
    }

    @Test
    fun `test addNote`() {
        val template = ChordTemplate()
        template.addInterval(4)
        template.addInterval(2)
        template.addInterval(3)
        assertEquals(listOf(2, 3, 4), template.intervals)
    }

    @Test
    fun `test addAll`() {
        val template = ChordTemplate()
        template.addAllIntervals(4, 5, 9, 1, 2)
        assertEquals(listOf(1, 2, 4, 5, 9), template.intervals)
    }

    @Test
    fun `test contains method true`() {
        val template = ChordTemplate()
        template.addInterval(5)
        template.contains(5)
    }

    @Test
    fun `test addNote throws DuplicateIntervalError`() {
        val template = ChordTemplate()
        template.addInterval(2)
        template.addInterval(5)
        assertFailsWith<IllegalStateException> { template.addInterval(2) }
    }

}