package com.example.notechaser.models.PatternEngine;

import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.PhraseTemplate;
import com.example.notechaser.RandomPatternGenerator;

import java.util.List;

public class PatternEngineImpl implements PatternEngine {

    // Todo: probably make this abstract eventually
    private RandomPatternGenerator mRandomPatternGenerator;

    // lower and upper bounds
    // list of modes
    // list of templates

    public PatternEngineImpl() {

    }

    @Override
    public boolean isSufficientSpace(int lowerBound, int upperBound) {
        // I don't think I need this
        return false;
    }

    @Override
    public Pattern generatePattern() {
        return null;
    }

    @Override
    public boolean isAnswerCorrect(List<Note> answer) {
        return false;
    }

    @Override
    public void addPhraseTemplate(PhraseTemplate toAdd) {

    }

    @Override
    public void removePhraseTemplate(PhraseTemplate toRemove) {

    }
}
