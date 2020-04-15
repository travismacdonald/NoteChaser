package com.example.notechaser.patterngenerator

import com.example.notechaser.patterngenerator.exceptions.DuplicateTemplateException
import com.example.notechaser.patterngenerator.exceptions.EmptyTemplateListException
import com.example.notechaser.patterngenerator.exceptions.InsufficientRangeException
import com.example.notechaser.patterngenerator.exceptions.InvalidRangeException
import java.util.ArrayList
import kotlin.random.Random

// Todo: address issues of tracking changes to templates already in generator's template list

class PatternGenerator(
        var lowerBound: Int = -1,
        var upperBound: Int = -1,
        private val _templates: ArrayList<PatternTemplate> = arrayListOf()
    ) {

    val templates: List<PatternTemplate>
        get() = _templates

    val size: Int
        get() = _templates.size

    fun findRangeRequired(): Int {
        if (_templates.isEmpty()) {
            throw EmptyTemplateListException("_templates does not contain any PatternTemplates.")
        }
        return _templates.maxBy { it.range }?.range!!
    }

    fun hasSufficientRange(): Boolean {
        return upperBound - lowerBound >= findRangeRequired()
    }

    fun generatePattern(): Pattern {
        if (!hasValidRange()) {
            throw InvalidRangeException(
                    "lowerBound is greater than upperBound." +
                            "\n\tlowerBound: $lowerBound" +
                            "\n\tupperBound: $upperBound"
            )
        } else if (!hasSufficientRange()) {
            throw InsufficientRangeException(
                    "Not enough range to generate Pattern." +
                            "\n\tRange required: ${findRangeRequired()}" +
                            "\n\tActual range: ${upperBound - lowerBound}"
            )
        } else {
            val template = templates[Random.nextInt(size)]
            // + 1 because pattern generation range has inclusive bounds
            val ix = Random.nextInt(lowerBound, (upperBound - template.range) + 1)
            return Pattern(template, ix)
        }
    }

    fun getPatternTemplateAt(ix: Int): PatternTemplate {
        return _templates[ix]
    }

    fun addPatternTemplate(template: PatternTemplate) {
        if (contains(template)) {
            throw DuplicateTemplateException("Tried to add equivalent template to _templates.")
        }
        _templates.add(template)
    }

    fun removePatternTemplateAt(ix: Int) {
        _templates.removeAt(ix)
    }

    fun isEmpty(): Boolean {
        return _templates.isEmpty()
    }

    fun isNotEmpty(): Boolean {
        return _templates.isNotEmpty()
    }

    fun contains(template: PatternTemplate): Boolean {
        return _templates.contains(template)
    }

    private fun hasValidRange(): Boolean {
        return lowerBound in 0..upperBound
    }

}