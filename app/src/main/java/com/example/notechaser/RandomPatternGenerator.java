package com.example.notechaser;

import com.example.keyfinder.Mode;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.PhraseTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPatternGenerator {

    private Random mRandom;

    private List<PhraseTemplate> mPhraseTemplates;

    private List<Mode> mModes;

    private int mLowerBound;

    private int mUpperBound;

    private int mOverallSpaceRequired;

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
        mOverallSpaceRequired = calculateOverallSpaceNeeded();
    }

    public void addPhraseTemplate(PhraseTemplate toAdd) {
        mPhraseTemplates.add(toAdd);
        mOverallSpaceRequired = calculateOverallSpaceNeeded();
    }

    public void removePhraseTemplate(PhraseTemplate toRemove) {
        mPhraseTemplates.remove(toRemove);
        mOverallSpaceRequired = calculateOverallSpaceNeeded();
    }

    public void addMode(List<Mode> modes) {
        mModes.addAll(modes);
        mOverallSpaceRequired = calculateOverallSpaceNeeded();
    }

    public void addMode(Mode mode) {
        mModes.add(mode);
        mOverallSpaceRequired = calculateOverallSpaceNeeded();
    }

    public void removeMode(Mode mode) {
        mModes.remove(mode);
        mOverallSpaceRequired = calculateOverallSpaceNeeded();
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
        return (mUpperBound - mLowerBound) >= mOverallSpaceRequired;
    }

    private int calculateOverallSpaceNeeded() {
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
