package com.example.notechaser.playablegenerator

import androidx.lifecycle.MutableLiveData
import com.example.notechaser.playablegenerator.exceptions.DuplicateTemplateException
import com.example.notechaser.playablegenerator.exceptions.EmptyTemplateListException
import com.example.notechaser.playablegenerator.exceptions.InsufficientRangeException
import com.example.notechaser.playablegenerator.exceptions.InvalidRangeException
import java.util.ArrayList
import kotlin.random.Random

// Todo: address issues of tracking changes to templates already in generator's template list

class PlayableGenerator(
        private val _templates: ArrayList<PlayableTemplate> = arrayListOf()) {

    val lowerBound = MutableLiveData(36)

    val upperBound = MutableLiveData(48)

    val templates: List<PlayableTemplate>
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
        return upperBound.value!! - lowerBound.value!! >= findRangeRequired()
    }

    fun generatePlayable(): Playable {
        if (!hasValidRange()) {
            throw InvalidRangeException(
                    "lowerBound is greater than upperBound." +
                            "\n\tlowerBound: ${lowerBound.value}" +
                            "\n\tupperBound: ${upperBound.value}"
            )
        } else if (!hasSufficientRange()) {
            throw InsufficientRangeException(
                    "Not enough range to generate Pattern." +
                            "\n\tRange required: ${findRangeRequired()}" +
                            "\n\tActual range: ${upperBound.value!! - lowerBound.value!!}"
            )
        } else {
            val template = templates[Random.nextInt(size)]
            // + 1 because pattern generation range has inclusive bounds
            val ix = Random.nextInt(lowerBound.value!!, (upperBound.value!! - template.range) + 1)
            return Playable(template, ix)
        }
    }

    fun getTemplateAt(ix: Int): PlayableTemplate {
        return _templates[ix]
    }

    fun addTemplate(template: PlayableTemplate) {
        if (contains(template)) {
            throw DuplicateTemplateException("Tried to add equivalent template to _templates.")
        }
        _templates.add(template)
    }

    fun removeTemplateAt(ix: Int) {
        _templates.removeAt(ix)
    }

    fun isEmpty(): Boolean {
        return _templates.isEmpty()
    }

    fun isNotEmpty(): Boolean {
        return _templates.isNotEmpty()
    }

    fun contains(template: PlayableTemplate): Boolean {
        return _templates.contains(template)
    }

    private fun hasValidRange(): Boolean {
        return lowerBound.value!! in 0..upperBound.value!!
    }

}