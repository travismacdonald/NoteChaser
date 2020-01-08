package com.example.notechaser.models.patternengine;

import android.util.Log;

import com.example.keyfinder.Mode;
import com.example.keyfinder.eartraining.IntervalTemplate;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.AbstractTemplate;
import com.example.notechaser.patterngenerator.IntervalPatternGenerator;
import com.example.notechaser.patterngenerator.AbstractPatternGenerator;


public class PatternEngineImpl implements PatternEngine {

    // ****************
    // MEMBER VARIABLES
    // ****************

    /**
     * Type of pattern generator used
     */
    private enum Type {
        DYNAMIC, FIXED
    }

    // Todo: make this abstract: RPG can then hold any kind of random pattern generator
    private AbstractPatternGenerator mAbstractPatternGenerator;

    private IntervalPatternGenerator mIntervalPatternGenerator;

    private Pattern mCurPattern;

    private Type mType;

    public PatternEngineImpl() {
        // Todo: clean this up
        mType = null;
        mAbstractPatternGenerator = new AbstractPatternGenerator();
        mIntervalPatternGenerator = new IntervalPatternGenerator();
        mCurPattern = null;
    }

//    @Override
//    public boolean isValid() {
//        if ()
//        if (!hasSufficientSpace())
//    }

    @Override
    public boolean hasSufficientSpace() {
        if (mType == null) {
            Log.d("bug", "Choose type of generator");
            return false;
        }
        else if (mType == Type.FIXED) {
            return mIntervalPatternGenerator.hasSufficientSpace();
        }
        else {
            return mAbstractPatternGenerator.hasSufficientSpace();
        }
    }

    @Override
    public Pattern generatePattern() {
        if (mType == null) {
            Log.d("bug", "Choose type of generator");
        }
        else if (mType == Type.FIXED) {
            mCurPattern = mIntervalPatternGenerator.generatePattern();
        }
        else {
            mCurPattern = mAbstractPatternGenerator.generatePattern();
        }
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
    public void removeIntervalTemplate(IntervalTemplate toRemove) {
        mIntervalPatternGenerator.removeIntervalTemplate(toRemove);
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

    @Override
    public void setTypeDynamic() {

    }

    @Override
    public void setTypeFixed() {

    }


}
