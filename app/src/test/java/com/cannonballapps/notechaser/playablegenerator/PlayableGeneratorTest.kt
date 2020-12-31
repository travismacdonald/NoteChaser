package com.cannonballapps.notechaser.playablegenerator


class PlayableGeneratorTest {
//
//    @Test
//    fun `test setUpperBound`() {
//        val generator = Generator()
//        generator.upperBound.value = 24
//        assertEquals(24, generator.upperBound.value)
//    }
//
//    @Test
//    fun `test getUpperBound default value`() {
//        val generator = Generator()
//        assertEquals(-1, generator.upperBound.value)
//    }
//
//    @Test
//    fun `test hasSufficientSpace should be true when required range equals bound range`() {
//        val generator = Generator()   // Bound range: 7
//        generator.lowerBound.value = 3
//        generator.upperBound.value = 10
//        val templateOne = PatternTemplate(arrayListOf(0, 7))   // template range: 7
//        val templateTwo = PatternTemplate(arrayListOf(3, 5))   // template range: 2
//        generator.addTemplate(templateOne)
//        generator.addTemplate(templateTwo)
//        assertTrue(generator.hasSufficientRange())
//    }
//
//    @Test
//    fun `test hasSufficientSpace should be false when required range exceeds bound range`() {
//        val generator = Generator()   // Bound range: 6
//        generator.lowerBound.value = 0
//        generator.upperBound.value = 6
//        val templateOne = PatternTemplate(arrayListOf(0, 7))   // template range: 7
//        val templateTwo = PatternTemplate(arrayListOf(3, 5))   // template range: 2
//        generator.addTemplate(templateOne)
//        generator.addTemplate(templateTwo)
//        assertFalse(generator.hasSufficientRange())
//    }
//
//    @Test
//    fun `test findRangeRequired throws exception when template list is empty`() {
//        val generator = Generator()
//        generator.lowerBound.value = 0
//        generator.upperBound.value = 8
//        assertFailsWith<EmptyTemplateListException> { generator.findRangeRequired() }
//    }
//
//    @Test
//    fun `test generatePattern throws exception with invalid range`() {
//        val generator = Generator() // lower bound > upper bound
//        generator.lowerBound.value = 5
//        generator.upperBound.value = 0
//        generator.addTemplate(PatternTemplate(arrayListOf(1, 2, 3)))
//        assertFailsWith<InvalidRangeException> { generator.generatePlayable() }
//    }
//
//    @Test
//    fun `test generatePattern throws exception with insufficient range`() {
//        val generator = Generator()
//        generator.lowerBound.value = 0
//        generator.upperBound.value = 8
//        generator.addTemplate(PatternTemplate(arrayListOf(1, 2, 3)))
//        generator.addTemplate(PatternTemplate(arrayListOf(0, 2, 9)))
//        assertFailsWith<InsufficientRangeException> { generator.generatePlayable() }
//    }
//
//    @Test
//    fun `test getPatternTemplateAt`() {
//        val generator = Generator()
//        val thisTemplate = PatternTemplate(arrayListOf(4, 5, 6))
//        val otherTemplate = PatternTemplate(arrayListOf(3, 4))
//        generator.addTemplate(thisTemplate)
//        generator.addTemplate(otherTemplate)
//        assertTrue(generator.getTemplateAt(1) == otherTemplate)
//    }
//
//    @Test
//    fun `test addPatternTemplate throws exception when equal template in generator`() {
//        val generator = Generator()
//        val thisTemplate = PatternTemplate(arrayListOf(0, 2, 4))
//        val otherTemplate = PatternTemplate(arrayListOf(0, 2, 4))
//        generator.addTemplate(thisTemplate)
//        assertFailsWith<DuplicateTemplateException> { generator.addTemplate(otherTemplate) }
//    }
//
//    @Test
//    fun `test removePatternTemplateAt`() {
//        val generator = Generator()
//        val thisTemplate = PatternTemplate(arrayListOf(0, 2, 4))
//        val otherTemplate = PatternTemplate(arrayListOf(5, 9))
//        generator.addTemplate(thisTemplate)
//        generator.addTemplate(otherTemplate)
//        generator.removeTemplateAt(0)
//        assertEquals(1, generator.size)
//    }
//
//    @Test
//    fun `test isEmpty true when no templates`() {
//        val generator = Generator()
//        assertTrue(generator.isEmpty())
//    }
//
//    @Test
//    fun `test isEmpty false when it contains templates`() {
//        val generator = Generator()
//        generator.addTemplate(PatternTemplate(arrayListOf(2, 4)))
//        assertFalse(generator.isEmpty())
//    }
//
//    @Test
//    fun `test isNotEmpty false when no templates`() {
//        val generator = Generator()
//        assertFalse(generator.isNotEmpty())
//    }
//
//    @Test
//    fun `test isNotEmpty true when it contains templates`() {
//        val generator = Generator()
//        generator.addTemplate(PatternTemplate(arrayListOf(2, 4)))
//        assertTrue(generator.isNotEmpty())
//    }
//
//    @Test
//    fun `test contains should contain equal template`() {
//        val generator = Generator()
//        val thisTemplate = PatternTemplate(arrayListOf(1, 2, 3))
//        val otherTemplate = PatternTemplate(arrayListOf(1, 2, 3)) // templates are equal
//        generator.addTemplate(thisTemplate)
//        assertTrue(generator.contains(otherTemplate))
//    }
//
//    @Test
//    fun `test size zero when empty`() {
//        val generator = Generator()
//        assertEquals(0, generator.size)
//    }
//
//    @Test
//    fun `test size changes when templates added`() {
//        val generator = Generator()
//        generator.addTemplate(PatternTemplate(arrayListOf(2, 9)))
//        assertEquals(1, generator.size)
//    }
//
//    // todo: find better way to test this
//    @Test
//    fun `bad test`() {
//        val generator = Generator()
//        generator.lowerBound.value = 6
//        generator.upperBound.value = 14
//        generator.addTemplate(PatternTemplate(arrayListOf(3, 0, 6)))
//        for (i in 0 until 100) {
//            println(generator.generatePlayable().toString())
//        }
//    }

}