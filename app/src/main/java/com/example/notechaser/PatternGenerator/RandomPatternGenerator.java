package com.example.notechaser.PatternGenerator;

import com.example.keyfinder.Mode;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.PhraseTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPatternGenerator {

    /**
     * For storing in memory.
     */
    private String name;

    private Random mRandom;

    private List<PhraseTemplate> mPhraseTemplates;

    private List<Mode> mModes;

    private int mLowerBound;

    private int mUpperBound;

    // Max space is worst case
    private int mMaxSpaceRequired;

    public RandomPatternGenerator() {
        this(-1, -1);
    }

    public RandomPatternGenerator(int lowerBound, int upperBound) {
        mPhraseTemplates = new ArrayList<>();
        mModes = new ArrayList<>();
        mLowerBound = lowerBound;
        mUpperBound = upperBound;
        mRandom = new Random();
    }

    public Pattern generatePattern() {
        if (hasSufficientSpace()) {
            // pick random template
            PhraseTemplate phraseTemplate = mPhraseTemplates.get(mRandom.nextInt(mPhraseTemplates.size()));
            // pick random mode
            Mode mode = mModes.get(mRandom.nextInt(mModes.size()));
            // pick random key
            // one of the ugliest lines of code ive seen
            int keyIx = mRandom.nextInt(mUpperBound - mLowerBound - Pattern.calculateMinSpaceRequired(phraseTemplate, mode) + 1) + mLowerBound;
            // return that shit
            return Pattern.generatePattern(phraseTemplate, mode, keyIx);
        }
        else {
            // what the fuck dude
            return null;
        }
    }

    public void addPhraseTemplate(List<PhraseTemplate> toAdd) {
        mPhraseTemplates.addAll(toAdd);
        mMaxSpaceRequired = calculateMaxSpaceRequired();
    }

    public void addPhraseTemplate(PhraseTemplate toAdd) {
        mPhraseTemplates.add(toAdd);
        mMaxSpaceRequired = calculateMaxSpaceRequired();
    }

    public void removePhraseTemplate(PhraseTemplate toRemove) {
        mPhraseTemplates.remove(toRemove);
        mMaxSpaceRequired = calculateMaxSpaceRequired();
    }

    public void addMode(List<Mode> modes) {
        mModes.addAll(modes);
        mMaxSpaceRequired = calculateMaxSpaceRequired();
    }

    public void addMode(Mode mode) {
        mModes.add(mode);
        mMaxSpaceRequired = calculateMaxSpaceRequired();
    }

    public void removeMode(Mode mode) {
        mModes.remove(mode);
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
        for (PhraseTemplate phraseTemplate : mPhraseTemplates) {
            for (Mode mode : mModes) {
                final int result = Pattern.calculateMinSpaceRequired(phraseTemplate, mode);
                if (result > maxSpace) {
                    maxSpace = result;
                }
            }
        }
        return maxSpace;
    }

}
