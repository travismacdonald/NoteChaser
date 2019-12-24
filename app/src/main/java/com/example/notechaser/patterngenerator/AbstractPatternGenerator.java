package com.example.notechaser.patterngenerator;

import com.example.keyfinder.Mode;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.AbstractTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstract Pattern Generator uses abstract patterns to randomly generate patterns.
 * Patterns will be generated in modes given by the user that are in the range given by the user.
 */
public class AbstractPatternGenerator {

    // ****************
    // MEMBER VARIABLES
    // ****************

    /**
     * For storing in memory.
     */
    private String mName;

    private Random mRandom;

    /**
     * Abstract templates used for pattern generation.
     */
    private List<AbstractTemplate> mAbstractTemplates;

    /**
     * Modes used for pattern generation.
     */
    private List<Mode> mModes;

    /**
     * Lowest note index used for pattern generation.
     */
    private int mLowerBound;

    /**
     * Highest note index used for pattern generation.
     */
    private int mUpperBound;

    /**
     * The minimum range required such that every mode/template combination can be
     * generated for at least one key.
     */
    private int mMinSpaceRequired;

    // ************
    // CONSTRUCTORS
    // ************

    public AbstractPatternGenerator() {
        this(-1, -1);
    }

    public AbstractPatternGenerator(int lowerBound, int upperBound) {
        mAbstractTemplates = new ArrayList<>();
        mModes = new ArrayList<>();
        mLowerBound = lowerBound;
        mUpperBound = upperBound;
        mRandom = new Random();
    }

    // **************
    // PUBLIC METHODS
    // **************

    // Todo: update the deprecated methods here.
    /**
     * Generates a pattern by choosing a random template, key, and  mode.
     *
     * @return generated pattern; ready for playback.
     */
    public Pattern generatePattern() {
        if (hasSufficientSpace()) {
            AbstractTemplate AbstractTemplate = mAbstractTemplates.get(mRandom.nextInt(mAbstractTemplates.size()));
            Mode mode = mModes.get(mRandom.nextInt(mModes.size()));
            int keyIx = mRandom.nextInt(mUpperBound - mLowerBound - Pattern.calculateMinSpaceRequired(AbstractTemplate, mode) + 1) + mLowerBound;
            return Pattern.generatePattern(AbstractTemplate, mode, keyIx);
        }
        else {
            return null;
        }
    }

    public void addAbstractTemplate(List<AbstractTemplate> toAdd) {
        mAbstractTemplates.addAll(toAdd);
        mMinSpaceRequired = calculateMinSpaceRequired();
    }

    public void addAbstractTemplate(AbstractTemplate toAdd) {
        mAbstractTemplates.add(toAdd);
        mMinSpaceRequired = calculateMinSpaceRequired();
    }

    public void removeAbstractTemplate(AbstractTemplate toRemove) {
        mAbstractTemplates.remove(toRemove);
        mMinSpaceRequired = calculateMinSpaceRequired();
    }

    public void addMode(List<Mode> modes) {
        this.mModes.addAll(modes);
        mMinSpaceRequired = calculateMinSpaceRequired();
    }

    public void addMode(Mode mode) {
        mModes.add(mode);
        mMinSpaceRequired = calculateMinSpaceRequired();
    }

    public void removeMode(Mode mode) {
        mModes.remove(mode);
        mMinSpaceRequired = calculateMinSpaceRequired();
    }

    public int getLowerBound() {
        return mLowerBound;
    }

    public void setLowerBound(int lowerBound) {
        mLowerBound = lowerBound;
    }

    public int getUpperBound() {
        return mUpperBound;
    }

    public void setUpperBound(int upperBound) {
        mUpperBound = upperBound;
    }

    public boolean hasSufficientSpace() {
        return (mUpperBound - mLowerBound) >= mMinSpaceRequired;
    }

    // ***************
    // PRIVATE METHODS
    // ***************

    /**
     * Searches every mode/template combination to find the pattern that has the largest range.
     */
    private int calculateMinSpaceRequired() {
        int maxSpace = -1;
        // Todo: better solution than brute force
        for (AbstractTemplate AbstractTemplate : mAbstractTemplates) {
            for (Mode mode : mModes) {
                final int result = Pattern.calculateMinSpaceRequired(AbstractTemplate, mode);
                if (result > maxSpace) {
                    maxSpace = result;
                }
            }
        }
        return maxSpace;
    }
}
