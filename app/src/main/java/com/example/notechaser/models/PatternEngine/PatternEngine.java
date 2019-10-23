package com.example.notechaser.models.PatternEngine;

import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.PhraseTemplate;

public interface PatternEngine {

    boolean isSufficientSpace(int lowerBound, int upperBound);

    // create and return new pattern
    Pattern generatePattern();

    // return the current pattern
    Pattern getPattern();

    void addPhraseTemplate(PhraseTemplate toAdd);

    void removePhraseTemplate(PhraseTemplate toRemove);

}
