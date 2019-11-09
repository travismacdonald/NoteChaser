package com.example.notechaser.patterngenerator;

import com.example.keyfinder.eartraining.Pattern;
import com.example.notechaser.data.NCIntervalTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntervalPatternGenerator {

    /**
     * For storing in memory.
     */
    private String mName;

    private Random mRandom;

    private List<NCIntervalTemplate> mIntervalTemplates;

    private int mLowerBound;

    private int mUpperBound;

    // Max space is worst case
    private int mMaxSpaceRequired;

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
            // pick mRandom template
            NCIntervalTemplate template = mIntervalTemplates.get(mRandom.nextInt(mIntervalTemplates.size()));
            // pick mRandom key
            // one of the ugliest lines of code ive seen
//            int keyIx = mRandom.nextInt(mUpperBound - mLowerBound - Pattern.calculateMinSpaceRequired(phraseTemplate, mode) + 1) + mLowerBound;
            int keyIx = mRandom.nextInt(mUpperBound - mLowerBound - template.getSpaceRequired() + 1) + mLowerBound;
            // return that shit
            return template.generatePattern(keyIx);
        }
        else {
            return null;
        }
    }

    public void addIntervalTemplate(NCIntervalTemplate toAdd) {
        mIntervalTemplates.add(toAdd);
        mMaxSpaceRequired = calculateMaxSpaceRequired();
    }

    public void removeIntervalTemplate(NCIntervalTemplate toRemove) {
        mIntervalTemplates.remove(toRemove);
        mMaxSpaceRequired = calculateMaxSpaceRequired();
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
        return (mUpperBound - mLowerBound) >= mMaxSpaceRequired;
    }

    private int calculateMaxSpaceRequired() {
        int maxSpace = -1;
        // Todo: better solution than brute force
        for (NCIntervalTemplate template : mIntervalTemplates) {
            final int space = template.getSpaceRequired();
            if (space > maxSpace) {
                maxSpace = space;
            }
        }
        return maxSpace;
    }


}
