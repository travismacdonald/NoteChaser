package com.example.notechaser.models.patternengine;

import com.example.keyfinder.Mode;
import com.example.keyfinder.eartraining.IntervalTemplate;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.AbstractTemplate;
import com.example.notechaser.patterngenerator.IntervalPatternGenerator;
import com.example.notechaser.patterngenerator.AbstractPatternGenerator;


public class PatternEngineImpl implements PatternEngine {

    private enum Type {
        PHRASE, INTERVAL
    }

    // Todo: make this abstract: RPG can then hold any kind of random pattern generator
    private AbstractPatternGenerator mAbstractPatternGenerator;

    private IntervalPatternGenerator mIntervalPatternGenerator;

    private Pattern mCurPattern;

    private Type mType;

    public PatternEngineImpl() {
        // Todo: clean this up
        mType = Type.INTERVAL;
        mAbstractPatternGenerator = new AbstractPatternGenerator();
        mIntervalPatternGenerator = new IntervalPatternGenerator();
        mCurPattern = null;
    }

    @Override
    public boolean hasSufficientSpace() {
        if (mType == Type.INTERVAL) {
            return mIntervalPatternGenerator.hasSufficientSpace();
        }
        return mAbstractPatternGenerator.hasSufficientSpace();
    }

    @Override
    public Pattern generatePattern() {
        if (mType == Type.INTERVAL) {
            mCurPattern = mIntervalPatternGenerator.generatePattern();
            return mCurPattern;
        }
        mCurPattern = mAbstractPatternGenerator.generatePattern();
        return mCurPattern;
    }

    @Override
    public Pattern getCurPattern() {
        return mCurPattern;
    }

    @Override
    public void addIntervalTemplate(IntervalTemplate toAdd) {
        mIntervalPatternGenerator.addIntervalTemplate(toAdd);
    }

    @Override
    public void addAbstractTemplate(AbstractTemplate toAdd) {
        mAbstractPatternGenerator.addAbstractTemplate(toAdd);
    }

    @Override
    public void removeAbstractTemplate(AbstractTemplate toRemove) {
        mAbstractPatternGenerator.removeAbstractTemplate(toRemove);
    }

    @Override
    public void setLowerBound(int lowerBound) {
        mIntervalPatternGenerator.setLowerBound(lowerBound);
        mAbstractPatternGenerator.setLowerBound(lowerBound);
    }

    @Override
    public void setUpperBound(int upperBound) {
        mIntervalPatternGenerator.setUpperBound(upperBound);
        mAbstractPatternGenerator.setUpperBound(upperBound);
    }

    @Override
    public void setBounds(int lowerBound, int upperBound) {
        mIntervalPatternGenerator.setUpperBound(upperBound);
        mIntervalPatternGenerator.setLowerBound(lowerBound);

        mAbstractPatternGenerator.setLowerBound(lowerBound);
        mAbstractPatternGenerator.setUpperBound(upperBound);
    }

    @Override
    public void addMode(Mode mode) {
        mAbstractPatternGenerator.addMode(mode);
    }

    @Override
    public void removeMode(Mode mode) {
        mAbstractPatternGenerator.removeMode(mode);
    }
}
