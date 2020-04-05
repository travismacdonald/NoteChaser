package com.example.notechaser.patterngenerator

import java.io.Serializable
import java.util.*

// Todo: change serializable to parcelable
class PatternGenerator @JvmOverloads constructor(lowerBound: Int = -1, upperBound: Int = -1) : Serializable {
    private val mName: String? = null
    private val mRandom: Random

    // Todo: change IntervalTemplate ta PatternTemplate
    private val mIntervalTemplates: MutableList<PatternTemplate>
    var lowerBound: Int
    var upperBound: Int
    private var mMinSpaceRequired = 0
    fun generatePattern(): Pattern? {
        return if (hasSufficientSpace()) {
            // Pick random template
            val template = mIntervalTemplates[mRandom.nextInt(mIntervalTemplates.size)]
            // Pick random key
//            int keyIx = mRandom.nextInt(mUpperBound - mLowerBound - template.getSpaceRequired() + 1) + mLowerBound;
            // return that shit
//            return template.generatePattern(keyIx);
//            return new Pattern(template, keyIx);
            null
        } else {
            null
        }
    }

    fun addIntervalTemplate(toAdd: PatternTemplate) {
        mIntervalTemplates.add(toAdd)
        mMinSpaceRequired = calcMinSpaceRequired()
    }

    fun removeIntervalTemplate(toRemove: PatternTemplate) {
        mIntervalTemplates.remove(toRemove)
        mMinSpaceRequired = calcMinSpaceRequired()
    }

    fun hasSufficientSpace(): Boolean {
        return upperBound - lowerBound >= mMinSpaceRequired
    }

    private fun calcMinSpaceRequired(): Int {
        var maxSpace = -1
        // Todo: better solution than linear search
        for (template in mIntervalTemplates) {
//            final int space = template.getSpaceRequired();
            val space = -69
            if (space > maxSpace) {
                maxSpace = space
            }
        }
        return maxSpace
    }

    init {
        mIntervalTemplates = ArrayList()
        this.lowerBound = lowerBound
        this.upperBound = upperBound
        mRandom = Random()
    }
}