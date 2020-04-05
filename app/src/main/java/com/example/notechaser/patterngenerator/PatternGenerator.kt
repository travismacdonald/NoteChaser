package com.example.notechaser.patterngenerator

import java.io.Serializable
import java.lang.Exception

// Todo: change serializable to parcelable
class PatternGenerator constructor(var lowerBound: Int = -1, var upperBound: Int = -1) : Serializable {

    private val templates = arrayListOf<PatternTemplate>()

    fun hasSufficientSpace(): Boolean {
        if (templates.isEmpty()) {
            throw EmptyTemplateListException("PatternGenerator does not contain any PatternTemplates.")
        } else {
            return upperBound - lowerBound > templates.maxBy { it.range }?.range!!
        }
    }

    fun generatePattern(): Pattern {
        return Pattern(PatternTemplate(), 1)
    }

    fun getPatternTemplateAt(ix: Int): PatternTemplate {
        return templates[ix]
    }

    fun addPatternTemplate(template: PatternTemplate) {
        // Todo: check for duplicates
        //           -> no point in having two templates that are the exact same
        templates.add(template)
    }

    fun removePatternTemplate(template: PatternTemplate) {
        templates.remove(template)
    }

    fun isEmpty(): Boolean {
        return templates.isEmpty()
    }

    fun isNotEmpty(): Boolean {
        return templates.isNotEmpty()
    }

}

class EmptyTemplateListException(message: String) : Exception()