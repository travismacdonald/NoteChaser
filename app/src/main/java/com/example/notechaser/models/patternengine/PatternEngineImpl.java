package com.example.notechaser.models.patternengine;

import android.util.Log;


import com.example.notechaser.patterngenerator.PatternGenerator;
//import com.example.notechaser.patterngenerator.AbstractPatternGenerator;
import com.example.notechaser.patterngenerator.Pattern;
import com.example.notechaser.patterngenerator.PatternTemplate;

import java.io.Serializable;


public class PatternEngineImpl implements PatternEngine, Serializable {

    private enum Type {
        DYNAMIC, FIXED
    }

    // Todo: make this abstract: RPG can then hold any kind of random pattern generator
//    private AbstractPatternGenerator mAbstractPatternGenerator;

    private PatternGenerator mPatternGenerator;

    private Pattern mCurPattern;

    private Type mType;

    public PatternEngineImpl() {
        // Todo: clean this up
        mType = null;
//        mAbstractPatternGenerator = new AbstractPatternGenerator();
        mPatternGenerator = new PatternGenerator();
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
            return mPatternGenerator.hasSufficientSpace();
        }
        else {
//            return mAbstractPatternGenerator.hasSufficientSpace();
            return false;
        }
    }

    @Override
    public Pattern generatePattern() {
        if (mType == null) {
            Log.d("bug", "Choose type of generator");
        }
        else if (mType == Type.FIXED) {
            mCurPattern = mPatternGenerator.generatePattern();
        }
        else {
//            mCurPattern = mAbstractPatternGenerator.generatePattern();
        }
        return mCurPattern;
    }

    @Override
    public Pattern getCurPattern() {
        return mCurPattern;
    }

    @Override
    public void addPatternTemplate(PatternTemplate toAdd) {
        mPatternGenerator.addPatternTemplate(toAdd);
    }

    @Override
    public void removePatternTemplate(PatternTemplate toRemove) {
        mPatternGenerator.removePatternTemplate(toRemove);
    }

//    @Override
//    public void addAbstractTemplate(AbstractTemplate toAdd) {
//        mAbstractPatternGenerator.addAbstractTemplate(toAdd);
//    }
//
//    @Override
//    public void removeAbstractTemplate(AbstractTemplate toRemove) {
//        mAbstractPatternGenerator.removeAbstractTemplate(toRemove);
//    }

    @Override
    public void setLowerBound(int lowerBound) {
        mPatternGenerator.setLowerBound(lowerBound);
//        mAbstractPatternGenerator.setLowerBound(lowerBound);
    }

    @Override
    public void setUpperBound(int upperBound) {
        mPatternGenerator.setUpperBound(upperBound);
//        mAbstractPatternGenerator.setUpperBound(upperBound);
    }

    @Override
    public void setBounds(int lowerBound, int upperBound) {
        mPatternGenerator.setUpperBound(upperBound);
        mPatternGenerator.setLowerBound(lowerBound);

//        mAbstractPatternGenerator.setLowerBound(lowerBound);
//        mAbstractPatternGenerator.setUpperBound(upperBound);
    }

//    @Override
//    public void addMode(Mode mode) {
//        mAbstractPatternGenerator.addMode(mode);
//    }
//
//    @Override
//    public void removeMode(Mode mode) {
//        mAbstractPatternGenerator.removeMode(mode);
//    }

    @Override
    public void setTypeDynamic() {
        mType = Type.DYNAMIC;
    }

    @Override
    public void setTypeFixed() {
        mType = Type.FIXED;
    }

}
