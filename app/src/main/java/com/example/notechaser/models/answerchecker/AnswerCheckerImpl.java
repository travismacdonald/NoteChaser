package com.example.notechaser.models.answerchecker;

import com.example.keyfinder.MusicTheory;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;
import com.example.notechaser.data.UserAnswer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Todo: overloads could be better

/**
 * Class that compares user answers with generated answers.
 * Methods contain different criteria for determining whether or not answers are valid.
 */
public class AnswerCheckerImpl implements AnswerChecker {

    // *****************
    // INTERFACE METHODS
    // *****************

    /**
     * Checks if patterns are the same; order matters. Must be same octave.
     */
    @Override
    public boolean samePatternSameOctave(Pattern expected, UserAnswer actual) {
        return expected.getNotes().equals(actual.getAnswer());
    }

    /**
     * Checks if patterns are the same; order matters. Can be any octave.
     */
    @Override
    public boolean samePatternAnyOctave(Pattern expected, UserAnswer actual) {
        return sameNotesAnyOctave(expected.getNotes(), actual.getAnswer());
    }

    /**
     * Checks if notes are the same; order doesn't matter. Must be same octave.
     */
    @Override
    public boolean sameNotesSameOctave(Pattern expected, UserAnswer actual) {
        List<Note> lhs = new ArrayList<>(expected.getNotes());
        List<Note> rhs = new ArrayList<>(actual.getAnswer());
        Collections.sort(lhs);
        Collections.sort(rhs);
        return sameNotesSameOctave(lhs, rhs);
    }

    /**
     * Checks if notes are the same; order doesn't matter. Can be any octave.
     */
    @Override
    public boolean sameNotesAnyOctave(Pattern expected, UserAnswer actual) {
        List<Note> lhs = new ArrayList<>(expected.getNotes());
        List<Note> rhs = new ArrayList<>(actual.getAnswer());
        Collections.sort(lhs);
        Collections.sort(rhs);
        return sameNotesAnyOctave(lhs, rhs);
    }

    // ***************
    // PRIVATE METHODS
    // ***************

    /**
     * Checks if notes are the same; order doesn't matter. Must be same octave.
     */
    private boolean sameNotesSameOctave(List<Note> expected, List<Note> actual) {
        return expected.equals(actual);
    }

    /**
     * Checks if notes are the same; order doesn't matter. Can be any octave.
     */
    private boolean sameNotesAnyOctave(List<Note> expected, List<Note> actual) {
        if (expected.size() != actual.size()) {
            return false;
        }
        // Same root
        else if (!haveSameStartingNote(expected, actual)) {
            return false;
        }
        else {
            for (int i = 0; i < expected.size() - 1; i++) {
                // Distance between each note must be the same
                if (expected.get(i).getIx() - expected.get(i + 1).getIx() !=
                        actual.get(i).getIx() - actual.get(i + 1).getIx()) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Checks if two answers contain the same starting note. Can be any octave.
     */
    private boolean haveSameStartingNote(List<Note> lhs, List<Note> rhs) {
        return ((lhs.get(0).getIx() % MusicTheory.TOTAL_NOTES) ==
                (rhs.get(0).getIx() % MusicTheory.TOTAL_NOTES));
    }
}
