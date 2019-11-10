package com.example.notechaser.models.patternengine;

import com.example.keyfinder.Mode;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.IntervalTemplate;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.PhraseTemplate;
import com.example.notechaser.data.NCIntervalTemplate;


public interface PatternEngine {

    // See if pattern has enough space to generate at least one pattern
    boolean hasSufficientSpace();

    // only return pattern, doesn't generate new one
    Pattern getCurPattern();

    // Generate and return random pattern
    Pattern generatePattern();

    void addIntervalTemplate(IntervalTemplate toAdd);

    void addPhraseTemplate(PhraseTemplate toAdd);

    void removePhraseTemplate(PhraseTemplate toRemove);

    void setLowerBound(int lowerBound);

    void setUpperBound(int upperBound);

    void setBounds(int lowerBound, int upperBound);

    void addMode(Mode mode);

    void removeMode(Mode mode);

}
