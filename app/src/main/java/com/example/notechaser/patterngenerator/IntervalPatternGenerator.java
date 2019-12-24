package com.example.notechaser.patterngenerator;

import com.example.keyfinder.eartraining.IntervalTemplate;
import com.example.keyfinder.eartraining.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Interval Pattern Generator uses a set sequence of intervals that can only be transposed.
 * (eg: 0 2 4 7 -> can be transposed statically, but the intervals cannot be modified.)
 */
public class IntervalPatternGenerator {

    // ****************
    // MEMBER VARIABLES
    // ****************

    /**
     * For storing in memory.
     */
    private String mName;

    private Random mRandom;

    /**
     * List of interval templates used for pattern generation.
     */
    private List<IntervalTemplate> mIntervalTemplates;

    /**
     * Lowest note index used for pattern generation.
     */
    private int mLowerBound;

    /**
     * Highest note index used for pattern generation.
     */
    private int mUpperBound;

    /**
     * The minimum range required such that every template can be
     * generated for at least one key.
     */
    private int mMinSpaceRequired;

    // ************
    // CONSTRUCTORS
    // ************

    public IntervalPatternGenerator() {
        this(-1, -1);
    }

    public IntervalPatternGenerator(int lowerBound, int upperBound) {
        mIntervalTemplates = new ArrayList<>();
        mLowerBound = lowerBound;
        mUpperBound = upperBound;
        mRandom = new Random();
    }

    // **************
    // PUBLIC METHODS
    // **************

    // Todo: simplify else statement
    /**
     * Chooses a interval template at random, picks a random key within the given range,
     * and transposes the interval template, resulting in a pattern ready for playback.
     *
     * @return pattern ready for playback.
     */
    public Pattern generatePattern() {
        if (hasSufficientSpace()) {
            // Pick random template
            IntervalTemplate template = mIntervalTemplates.get(mRandom.nextInt(mIntervalTemplates.size()));
            // Pick random key
            int keyIx = mRandom.nextInt(mUpperBound - mLowerBound - template.getSpaceRequired() + 1) + mLowerBound;
            // return that shit
//            return template.generatePattern(keyIx);
            return new Pattern(template, keyIx);
        }
        else {
            return null;
        }
    }

    /**
     * Add interval template to generator.
     */
    public void addIntervalTemplate(IntervalTemplate toAdd) {
        mIntervalTemplates.add(toAdd);
        mMinSpaceRequired = calcMinSpaceRequired();
    }

    /**
     * Remove interval template from generator.
     */
    public void removeIntervalTemplate(IntervalTemplate toRemove) {
        mIntervalTemplates.remove(toRemove);
        mMinSpaceRequired = calcMinSpaceRequired();
    }

    public boolean hasSufficientSpace() {
        return (mUpperBound - mLowerBound) >= mMinSpaceRequired;
    }

    // *******************
    // GETTERS AND SETTERS
    // *******************

    /**
     * Get lower bound for pattern range.
     */
    public int getLowerBound() {
        return mLowerBound;
    }

    /**
     * Set lower bound for pattern range.
     */
    public void setLowerBound(int lowerBound) {
        mLowerBound = lowerBound;
    }

    /**
     * Get upper bound for pattern range.
     */
    public int getUpperBound() {
        return mUpperBound;
    }

    /**
     * Set upper bound for pattern range.
     */
    public void setUpperBound(int upperBound) {
        mUpperBound = upperBound;
    }

    // ***************
    // PRIVATE METHODS
    // ***************

    /**
     * Calculates the minimum space required such that every interval template has space
     * for at least one key.
     */
    private int calcMinSpaceRequired() {
        int maxSpace = -1;
        // Todo: better solution than linear search
        for (IntervalTemplate template : mIntervalTemplates) {
            final int space = template.getSpaceRequired();
            if (space > maxSpace) {
                maxSpace = space;
            }
        }
        return maxSpace;
    }
}
