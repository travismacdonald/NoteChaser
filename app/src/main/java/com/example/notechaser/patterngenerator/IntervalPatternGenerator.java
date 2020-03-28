package com.example.notechaser.patterngenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


// Todo: change serializable to parcelable
public class IntervalPatternGenerator implements Serializable {

    private String mName;

    private Random mRandom;

    // Todo: change IntervalTemplate ta PatternTemplate
    private List<PatternTemplate> mIntervalTemplates;

    private int mLowerBound;

    private int mUpperBound;

    private int mMinSpaceRequired;

    public IntervalPatternGenerator() {
        this(-1, -1);
    }

    public IntervalPatternGenerator(int lowerBound, int upperBound) {
        mIntervalTemplates = new ArrayList<>();
        mLowerBound = lowerBound;
        mUpperBound = upperBound;
        mRandom = new Random();
    }

    public Pattern generatePattern() {
        if (hasSufficientSpace()) {
            // Pick random template
            PatternTemplate template = mIntervalTemplates.get(mRandom.nextInt(mIntervalTemplates.size()));
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

    public void addIntervalTemplate(PatternTemplate toAdd) {
        mIntervalTemplates.add(toAdd);
        mMinSpaceRequired = calcMinSpaceRequired();
    }

    public void removeIntervalTemplate(PatternTemplate toRemove) {
        mIntervalTemplates.remove(toRemove);
        mMinSpaceRequired = calcMinSpaceRequired();
    }

    public boolean hasSufficientSpace() {
        return (mUpperBound - mLowerBound) >= mMinSpaceRequired;
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

    private int calcMinSpaceRequired() {
        int maxSpace = -1;
        // Todo: better solution than linear search
        for (PatternTemplate template : mIntervalTemplates) {
            final int space = template.getSpaceRequired();
            if (space > maxSpace) {
                maxSpace = space;
            }
        }
        return maxSpace;
    }
}
