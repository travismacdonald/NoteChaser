package com.example.notechaser.models.answerchecker;

import com.example.keyfinder.eartraining.Pattern;
import com.example.notechaser.data.UserAnswer;

public interface AnswerChecker {

    boolean samePatternSameOctave(Pattern expected, UserAnswer actual);

    boolean samePatternAnyOctave(Pattern expected, UserAnswer actual);

    boolean sameNotesSameOctave(Pattern expected, UserAnswer actual);

    boolean sameNotesAnyOctave(Pattern expected, UserAnswer actual);

}
