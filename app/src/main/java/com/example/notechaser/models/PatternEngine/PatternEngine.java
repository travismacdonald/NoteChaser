package com.example.notechaser.models.PatternEngine;

import com.example.keyfinder.Mode;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.PhraseTemplate;

import java.util.List;

public interface PatternEngine {

    boolean isSufficientSpace(int lowerBound, int upperBound);

    Pattern generatePattern();

    boolean isAnswerCorrect(List<Note> answer);

    void addPhraseTemplate(PhraseTemplate toAdd);

    void removePhraseTemplate(PhraseTemplate toRemove);

    void setLowerBound(int lowerBound);

    void setUpperBound(int upperBound);

    void addMode(Mode mode);

    void removeMode(Mode mode);

}
