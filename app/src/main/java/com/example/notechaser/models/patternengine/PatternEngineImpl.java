package com.example.notechaser.models.patternengine;

import com.example.keyfinder.Mode;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.PhraseTemplate;
import com.example.notechaser.patterngenerator.RandomPatternGenerator;

import java.util.List;

public class PatternEngineImpl implements PatternEngine {

    // Todo: probably make this abstract eventually
    private RandomPatternGenerator mRandomPatternGenerator;
    private Pattern mCurPattern;

    public PatternEngineImpl() {
        mRandomPatternGenerator = new RandomPatternGenerator();
        mCurPattern = null;
    }

    @Override
    public boolean hasSufficientSpace() {
        return mRandomPatternGenerator.hasSufficientSpace();
    }

    @Override
    public void generatePattern() {
        mCurPattern = mRandomPatternGenerator.generatePattern();
    }

    @Override
    public Pattern getCurPattern() {
        return mCurPattern;
    }

    @Override
    public boolean isAnswerCorrect(List<Note> answer) {
        return mCurPattern.getNotes().equals(answer);
    }

    @Override
    public void addPhraseTemplate(PhraseTemplate toAdd) {
        mRandomPatternGenerator.addPhraseTemplate(toAdd);
    }

    @Override
    public void removePhraseTemplate(PhraseTemplate toRemove) {
        mRandomPatternGenerator.removePhraseTemplate(toRemove);
    }

    @Override
    public void setLowerBound(int lowerBound) {
        mRandomPatternGenerator.setLowerBound(lowerBound);
    }

    @Override
    public void setUpperBound(int upperBound) {
        mRandomPatternGenerator.setUpperBound(upperBound);
    }

    @Override
    public void setBounds(int lowerBound, int upperBound) {
        mRandomPatternGenerator.setLowerBound(lowerBound);
        mRandomPatternGenerator.setUpperBound(upperBound);
    }

    @Override
    public void addMode(Mode mode) {
        mRandomPatternGenerator.addMode(mode);
    }

    @Override
    public void removeMode(Mode mode) {
        mRandomPatternGenerator.removeMode(mode);
    }
}
