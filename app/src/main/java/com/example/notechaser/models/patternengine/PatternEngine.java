package com.example.notechaser.models.patternengine;

import com.example.keyfinder.Mode;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.PhraseTemplate;

import java.util.List;

public interface PatternEngine {

    // See if pattern has enough space to generate at least one pattern
    boolean hasSufficientSpace();

    // only return pattern, doesn't generate new one
    Pattern getCurPattern();

    // Generate and return random pattern
    void generatePattern();

    // Check if user answer is same as notes played
    boolean isAnswerCorrect(List<Note> answer);


    void addPhraseTemplate(PhraseTemplate toAdd);

    void removePhraseTemplate(PhraseTemplate toRemove);

    void setLowerBound(int lowerBound);

    void setUpperBound(int upperBound);

    void setBounds(int lowerBound, int upperBound);

    void addMode(Mode mode);

    void removeMode(Mode mode);

}
