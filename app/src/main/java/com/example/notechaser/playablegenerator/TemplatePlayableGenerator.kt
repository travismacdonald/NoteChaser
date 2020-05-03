package com.example.notechaser.playablegenerator

import androidx.lifecycle.MutableLiveData
import com.example.notechaser.playablegenerator.exceptions.DuplicateTemplateException
import java.lang.IllegalStateException
import kotlin.random.Random

// TODO: Rename Class to something more precise / less ambiguous
// TODO: This is gonna take some work
class TemplatePlayableGenerator() : PlayableGenerator {

    /**
     * Type RANGE: patterns are always within lower (inclusive) and upper (inclusive) bound
     * Type FIXED_ROOT: pattern's lowest note is always rootIx
     * Type FIXED_STARTING_NOTE: playable's first note is always startingIx
     */
    var rangeType = MutableLiveData(GeneratorRangeType.BOUNDED)

    val lowerBound = MutableLiveData(36)

    val upperBound = MutableLiveData(48)

    /**
     * Index for fixed root playable's
     */
    val rootIx = MutableLiveData(36)

    /**
     * Index for fixed starting note playables.
     */
    val startingIx = MutableLiveData(36)

    private val _templates: MutableList<PlayableTemplate> = arrayListOf()

    val templates: List<PlayableTemplate>
        get() = _templates

    val numTemplates: Int
        get() = _templates.size

    private var rangeRequired: Int = -1

    private fun calcRangeRequired() {
        if (_templates.isEmpty()) {
            throw IllegalStateException("_templates does not contain any PatternTemplates.")
        }
        rangeRequired = _templates.maxBy { it.range }?.range!!
    }

    fun hasSufficientRange(): Boolean {
        return upperBound.value!! - lowerBound.value!! >= rangeRequired
    }

    override fun setupGenerator() {

    }

    /**
     * Function determines if patterns can be safely generated.
     */
    fun isValid(): Boolean {
        if (rangeType.value == GeneratorRangeType.BOUNDED && !hasSufficientRange()) {
            return false
        }
        if (_templates.isEmpty()) {
            return false;
        }
        return true;
    }

    override fun generatePlayable(): Playable {
            val template = templates[Random.nextInt(numTemplates)]
            // + 1 because pattern generation range has inclusive bounds
            val ix = Random.nextInt(lowerBound.value!!, (upperBound.value!! - template.range) + 1)
            return Playable(template, ix)
//        }
    }

    fun getTemplateAt(ix: Int): PlayableTemplate {
        return _templates[ix]
    }

    fun addTemplate(template: PlayableTemplate, ix: Int = numTemplates) {
        if (contains(template)) {
            throw DuplicateTemplateException("Tried to add equivalent template to _templates.")
        }
        _templates.add(ix, template)
        calcRangeRequired()
    }

    fun removeTemplateAt(ix: Int) {
        _templates.removeAt(ix)

        calcRangeRequired()
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