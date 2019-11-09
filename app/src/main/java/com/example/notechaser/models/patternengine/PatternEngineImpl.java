package com.example.notechaser.models.patternengine;

import com.example.keyfinder.Mode;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.PhraseTemplate;
import com.example.notechaser.data.NCIntervalTemplate;
import com.example.notechaser.patterngenerator.IntervalPatternGenerator;
import com.example.notechaser.patterngenerator.TemplatePatternGenerator;


public class PatternEngineImpl implements PatternEngine {

    private enum Type {
        PHRASE, INTERVAL
    }

    // Todo: make this abstract: RPG can then hold any kind of random pattern generator
    private TemplatePatternGenerator mTemplatePatternGenerator;

    private IntervalPatternGenerator mIntervalPatternGenerator;

    private Pattern mCurPattern;

    private Type mType;

    public PatternEngineImpl() {
        // Todo: clean this up
        mType = Type.INTERVAL;
        mTemplatePatternGenerator = new TemplatePatternGenerator();
        mIntervalPatternGenerator = new IntervalPatternGenerator();
        mCurPattern = null;
    }

    @Override
    public boolean hasSufficientSpace() {
        if (mType == Type.INTERVAL) {
            return mIntervalPatternGenerator.hasSufficientSpace();
        }
        return mTemplatePatternGenerator.hasSufficientSpace();
    }

    @Override
    public Pattern generatePattern() {
        if (mType == Type.INTERVAL) {
            mCurPattern = mIntervalPatternGenerator.generatePattern();
            return mCurPattern;
        }
        mCurPattern = mTemplatePatternGenerator.generatePattern();
        return mCurPattern;
    }

    @Override
    public Pattern getCurPattern() {
        return mCurPattern;
    }

    @Override
    public void addIntervalTemplate(NCIntervalTemplate toAdd) {
        mIntervalPatternGenerator.addIntervalTemplate(toAdd);
    }

    @Override
    public void addPhraseTemplate(PhraseTemplate toAdd) {
        mTemplatePatternGenerator.addPhraseTemplate(toAdd);
    }

    @Override
    public void removePhraseTemplate(PhraseTemplate toRemove) {
        mTemplatePatternGenerator.removePhraseTemplate(toRemove);
    }

    @Override
    public void setLowerBound(int lowerBound) {
        mIntervalPatternGenerator.setLowerBound(lowerBound);
        mTemplatePatternGenerator.setLowerBound(lowerBound);
    }

    @Override
    public void setUpperBound(int upperBound) {
        mIntervalPatternGenerator.setUpperBound(upperBound);
        mTemplatePatternGenerator.setUpperBound(upperBound);
    }

    @Override
    public void setBounds(int lowerBound, int upperBound) {
        mIntervalPatternGenerator.setUpperBound(upperBound);
        mIntervalPatternGenerator.setLowerBound(lowerBound);

        mTemplatePatternGenerator.setLowerBound(lowerBound);
        mTemplatePatternGenerator.setUpperBound(upperBound);
    }

    @Override
    public void addMode(Mode mode) {
        mTemplatePatternGenerator.addMode(mode);
    }

    @Override
    public void removeMode(Mode mode) {
        mTemplatePatternGenerator.removeMode(mode);
    }
}
