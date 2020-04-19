package com.example.notechaser.patterngenerator

import com.example.notechaser.patterngenerator.exceptions.DuplicateTemplateException
import com.example.notechaser.patterngenerator.exceptions.EmptyTemplateListException
import com.example.notechaser.patterngenerator.exceptions.InsufficientRangeException
import com.example.notechaser.patterngenerator.exceptions.InvalidRangeException
import org.junit.Test

import org.junit.Assert.*
import kotlin.test.assertFailsWith

class PatternGeneratorTest {

    @Test
    fun `test setUpperBound`() {
        val generator = PatternGenerator()
        generator._upperBound = 24
        assertEquals(24, generator._upperBound)
    }

    @Test
    fun `test getUpperBound default value`() {
        val generator = PatternGenerator()
        assertEquals(-1, generator._upperBound)
    }

    @Test
    fun `test hasSufficientSpace should be true when required range equals bound range`() {
        val generator = PatternGenerator(3, 10)   // Bound range: 7
        val templateOne = PatternTemplate(arrayListOf(0, 7))   // template range: 7
        val templateTwo = PatternTemplate(arrayListOf(3, 5))   // template range: 2
        generator.addPatternTemplate(templateOne)
        generator.addPatternTemplate(templateTwo)
        assertTrue(generator.hasSufficientRange())
    }

    @Test
    fun `test hasSufficientSpace should be false when required range exceeds bound range`() {
        val generator = PatternGenerator(0, 6)   // Bound range: 6
        val templateOne = PatternTemplate(arrayListOf(0, 7))   // template range: 7
        val templateTwo = PatternTemplate(arrayListOf(3, 5))   // template range: 2
        generator.addPatternTemplate(templateOne)
        generator.addPatternTemplate(templateTwo)
        assertFalse(generator.hasSufficientRange())
    }

    @Test
    fun `test findRangeRequired throws exception when template list is empty`() {
        val generator = PatternGenerator(0, 8)
        assertFailsWith<EmptyTemplateListException> { generator.findRangeRequired() }
    }

    @Test
    fun `test generatePattern throws exception with invalid range`() {
        val generator = PatternGenerator(5, 0) // lower bound > upper bound
        generator.addPatternTemplate(PatternTemplate(arrayListOf(1, 2, 3)))
        assertFailsWith<InvalidRangeException> { generator.generatePlayable() }
    }

    @Test
    fun `test generatePattern throws exception with insufficient range`() {
        val generator = PatternGenerator(0, 8)
        generator.addPatternTemplate(PatternTemplate(arrayListOf(1, 2, 3)))
        generator.addPatternTemplate(PatternTemplate(arrayListOf(0, 2, 9)))
        assertFailsWith<InsufficientRangeException> { generator.generatePlayable() }
    }

    @Test
    fun `test getPatternTemplateAt`() {
        val generator = PatternGenerator()
        val thisTemplate = PatternTemplate(arrayListOf(4, 5, 6))
        val otherTemplate = PatternTemplate(arrayListOf(3, 4))
        generator.addPatternTemplate(thisTemplate)
        generator.addPatternTemplate(otherTemplate)
        assertTrue(generator.getPatternTemplateAt(1) == otherTemplate)
    }

    @Test
    fun `test addPatternTemplate throws exception when equal template in generator`() {
        val generator = PatternGenerator()
        val thisTemplate = PatternTemplate(arrayListOf(0, 2, 4))
        val otherTemplate = PatternTemplate(arrayListOf(0, 2, 4))
        generator.addPatternTemplate(thisTemplate)
        assertFailsWith<DuplicateTemplateException> { generator.addPatternTemplate(otherTemplate) }
    }

    @Test
    fun `test removePatternTemplateAt`() {
        val generator = PatternGenerator()
        val thisTemplate = PatternTemplate(arrayListOf(0, 2, 4))
        val otherTemplate = PatternTemplate(arrayListOf(5, 9))
        generator.addPatternTemplate(thisTemplate)
        generator.addPatternTemplate(otherTemplate)
        generator.removePatternTemplateAt(0)
        assertEquals(1, generator.size)
    }

    @Test
    fun `test isEmpty true when no templates`() {
        val generator = PatternGenerator()
        assertTrue(generator.isEmpty())
    }

    @Test
    fun `test isEmpty false when it contains templates`() {
        val generator = PatternGenerator()
        generator.addPatternTemplate(PatternTemplate(arrayListOf(2, 4)))
        assertFalse(generator.isEmpty())
    }

    @Test
    fun `test isNotEmpty false when no templates`() {
        val generator = PatternGenerator()
        assertFalse(generator.isNotEmpty())
    }

    @Test
    fun `test isNotEmpty true when it contains templates`() {
        val generator = PatternGenerator()
        generator.addPatternTemplate(PatternTemplate(arrayListOf(2, 4)))
        assertTrue(generator.isNotEmpty())
    }

    @Test
    fun `test contains should contain equal template`() {
        val generator = PatternGenerator()
        val thisTemplate = PatternTemplate(arrayListOf(1, 2, 3))
        val otherTemplate = PatternTemplate(arrayListOf(1, 2, 3)) // templates are equal
        generator.addPatternTemplate(thisTemplate)
        assertTrue(generator.contains(otherTemplate))
    }

    @Test
    fun `test size zero when empty`() {
        val generator = PatternGenerator()
        assertEquals(0, generator.size)
    }

    @Test
    fun `test size changes when templates added`() {
        val generator = PatternGenerator()
        generator.addPatternTemplate(PatternTemplate(arrayListOf(2, 9)))
        assertEquals(1, generator.size)
    }

    // todo: find better way to test this
    @Test
    fun `bad test`() {
        val generator = PatternGenerator(6, 14)
        generator.addPatternTemplate(PatternTemplate(arrayListOf(3, 0, 6)))
        for (i in 0 until 100) {
            println(generator.generatePlayable().toString())
        }
    }

}