package com.example.notechaser.models.answerchecker;


import com.example.notechaser.data.UserAnswer;
import com.example.notechaser.patterngenerator.Pattern;

/**
 * Interface for answer checker model.
 */
public interface AnswerChecker {

    boolean samePatternSameOctave(Pattern expected, UserAnswer actual);

    boolean samePatternAnyOctave(Pattern expected, UserAnswer actual);

    boolean sameNotesSameOctave(Pattern expected, UserAnswer actual);

    boolean sameNotesAnyOctave(Pattern expected, UserAnswer actual);

}
