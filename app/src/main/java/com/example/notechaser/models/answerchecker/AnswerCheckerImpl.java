package com.example.notechaser.models.answerchecker;

import com.example.keyfinder.MusicTheory;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;
import com.example.notechaser.data.UserAnswer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnswerCheckerImpl implements AnswerChecker {

    @Override
    public boolean isCorrect(Pattern expected, UserAnswer actual) {
        return expected.getNotes().equals(actual.getAnswer());
    }

    @Override
    public boolean isCorrectAnyOctave(Pattern expected, UserAnswer actual) {
        return isCorrectAnyOctave(expected.getNotes(), actual.getAnswer());
//        if (expected.size() != actual.size()) {
//            return false;
//        }
//        // Same root
//        else if (!areSameKey(expected, actual)) {
//            return false;
//        }
//        else {
//            List<Note> expectedNotes = expected.getNotes();
//            List<Note> actualNotes = actual.getAnswer();
//            for (int i = 0; i < expected.size() - 1; i++) {
//                // Distance between each note must be the same
//                if (expectedNotes.get(i).getIx() - expectedNotes.get(i + 1).getIx() !=
//                        actualNotes.get(i).getIx() - actualNotes.get(i + 1).getIx()) {
//                    return false;
//                }
//            }
//            return true;
//        }
    }

    @Override
    public boolean sameNotes(Pattern expected, UserAnswer actual) {
        List<Note> lhs = new ArrayList<>(expected.getNotes());
        List<Note> rhs = new ArrayList<>(actual.getAnswer());
        Collections.sort(lhs);
        Collections.sort(rhs);
        return isCorrect(lhs, rhs);
    }

    @Override
    public boolean sameNotesAnyOctave(Pattern expected, UserAnswer actual) {
        List<Note> lhs = new ArrayList<>(expected.getNotes());
        List<Note> rhs = new ArrayList<>(actual.getAnswer());
        Collections.sort(lhs);
        Collections.sort(rhs);
        return isCorrectAnyOctave(lhs, rhs);
    }

    private boolean isCorrect(List<Note> expected, List<Note> actual) {
        return expected.equals(actual);
    }

//    private boolean areSameKey(Pattern lhs, UserAnswer rhs) {
//        return ((lhs.getNotes().get(0).getIx() % MusicTheory.TOTAL_NOTES) ==
//                (rhs.getAnswer().get(0).getIx() % MusicTheory.TOTAL_NOTES));
//    }

    private boolean isCorrectAnyOctave(List<Note> expected, List<Note> actual) {
        if (expected.size() != actual.size()) {
            return false;
        }
        // Same root
        else if (!areSameKey(expected, actual)) {
            return false;
        }
        else {
//            List<Note> expectedNotes = expected.getNotes();
//            List<Note> actualNotes = actual.getAnswer();
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

    private boolean areSameKey(List<Note> lhs, List<Note> rhs) {
        return ((lhs.get(0).getIx() % MusicTheory.TOTAL_NOTES) ==
                (rhs.get(0).getIx() % MusicTheory.TOTAL_NOTES));
    }
}
