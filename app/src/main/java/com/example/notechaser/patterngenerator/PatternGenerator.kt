package com.example.notechaser.patterngenerator

import java.io.Serializable
import java.lang.Exception
import kotlin.random.Random

// Todo: address issues of tracking changes to templates already in generator's template list

// Todo: change serializable to parcelable
class PatternGenerator (var lowerBound: Int = -1, var upperBound: Int = -1) : Serializable {

    // Todo: find way to clean this up
    private val invalidRangeExceptionMsg: String
        get() = "lowerBound is greater than upperBound." +
                "\n\tlowerBound: $lowerBound" +
                "\n\tupperBound: $upperBound"

    private val emptyTemplateListExceptionMsg: String
        get() = "_templates does not contain any PatternTemplates."

    private val duplicateTemplateExceptionMsg: String
        get() = "Tried to add equivalent template to _templates."

    private val insufficientRangeExceptionMsg: String
        get() = "Not enough range to generate Pattern." +
                "\n\tRange required: ${findRangeRequired()}" +
                "\n\tActual range: ${upperBound - lowerBound}"

    private val _templates = arrayListOf<PatternTemplate>()

    val templates: List<PatternTemplate>
        get() = _templates

    val size: Int
        get() = _templates.size

    fun findRangeRequired(): Int {
        if (_templates.isEmpty()) {
            throw EmptyTemplateListException(emptyTemplateListExceptionMsg)
        }
        return _templates.maxBy { it.range }?.range!!
    }

    fun hasSufficientRange(): Boolean {
        return upperBound - lowerBound >= findRangeRequired()
    }

    fun generatePattern(): Pattern {
        if (!hasValidRange()) throw InvalidRangeException(invalidRangeExceptionMsg)
        else if (!hasSufficientRange()) throw InsufficientRangeException(insufficientRangeExceptionMsg)
        else {
            val template = templates[Random.nextInt(size)]
            // +1 because pattern generation range has ixclusive bounds
            val ix = Random.nextInt(lowerBound, (upperBound - template.range) + 1)
            return Pattern(template, ix)
        }
    }

    fun getPatternTemplateAt(ix: Int): PatternTemplate {
        return _templates[ix]
    }

    fun addPatternTemplate(template: PatternTemplate) {
        if (contains(template)) throw DuplicateTemplateException(duplicateTemplateExceptionMsg)
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

class EmptyTemplateListException(message: String) : Exception(message)

class DuplicateTemplateException(message: String) : Exception(message)

class InvalidRangeException(message: String) : Exception(message)

class InsufficientRangeException(message: String) : Exception(message)