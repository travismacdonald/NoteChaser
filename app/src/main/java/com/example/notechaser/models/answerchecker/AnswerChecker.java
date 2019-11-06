package com.example.notechaser.models.answerchecker;

import com.example.keyfinder.eartraining.Pattern;
import com.example.notechaser.data.UserAnswer;

public interface AnswerChecker {

    boolean isCorrect(Pattern expected, UserAnswer actual);

    boolean isCorrectAnyOctave(Pattern expected, UserAnswer actual);

    boolean sameNotes(Pattern expected, UserAnswer actual);

    boolean sameNotesAnyOctave(Pattern expected, UserAnswer actual);

}
