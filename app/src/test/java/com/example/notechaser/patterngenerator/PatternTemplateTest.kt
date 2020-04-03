package com.example.notechaser.patterngenerator

import org.junit.Assert.*
import org.junit.Test

class PatternTemplateTest {

    @Test
    fun testInitTransposeNone() {
        val template = PatternTemplate(arrayListOf(0, 2, 4))
        assertEquals(arrayListOf(0, 2, 4), template.intervals)
    }

    @Test
    fun testInitTransposeDown() {
        val template = PatternTemplate(arrayListOf(1, 3, 5))
        assertEquals(arrayListOf(0, 2, 4), template.intervals)
    }

    @Test
    fun testInitTransposeUp() {
        val template = PatternTemplate(arrayListOf(-1, 1, 3))
        assertEquals(arrayListOf(0, 2, 4), template.intervals)
    }

    @Test
    fun testInitTransposeNoneRange() {
        val template = PatternTemplate(arrayListOf(0, 2, 4))
        assertEquals(4, template.range)
    }

    @Test
    fun testInitTransposeDownRange() {
        val template = PatternTemplate(arrayListOf(1, 3, 5))
        assertEquals(4, template.range)
    }

    @Test
    fun testInitTransposeUpRange() {
        val template = PatternTemplate(arrayListOf(-1, 1, 3))
        assertEquals(4, template.range)
    }

    @Test
    fun testInitTransposeNoneMid() {
        val template = PatternTemplate(arrayListOf(2, 0, 4))
        assertEquals(arrayListOf(2, 0, 4), template.intervals)
    }

    @Test
    fun testInitTransposeDownMid() {
        val template = PatternTemplate(arrayListOf(3, 1, 5))
        assertEquals(arrayListOf(2, 0, 4), template.intervals)
    }

    @Test
    fun testInitTransposeUpMid() {
        val template = PatternTemplate(arrayListOf(1, -1, 3))
        assertEquals(arrayListOf(2, 0, 4), template.intervals)
    }

}