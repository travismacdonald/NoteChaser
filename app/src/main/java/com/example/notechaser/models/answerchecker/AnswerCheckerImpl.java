package com.example.notechaser.models.answerchecker;

import android.util.Log;

import com.example.keyfinder.MusicTheory;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;
import com.example.notechaser.data.UserAnswer;

import java.util.List;

public class AnswerCheckerImpl implements AnswerChecker {

    @Override
    public boolean isCorrect(Pattern expected, UserAnswer actual) {
        return expected.getNotes().equals(actual.getAnswer());
    }

    @Override
    public boolean isCorrectAnyOctave(Pattern expected, UserAnswer actual) {
        Log.d("d-bug", expected.toString() + '\n' + actual.toString());
        if (expected.size() != actual.size()) {
            Log.d("d-bug", "dif size");
            return false;
        }
        // Same root
        else if (!areSameKey(expected, actual)) {
            Log.d("d-bug", "dif key");
            return false;
        }
        else {
            List<Note> expectedNotes = expected.getNotes();
            List<Note> actualNotes = actual.getAnswer();
            for (int i = 0; i < expected.size() - 1; i++) {
                // Distance between each note must be the same
                if (expectedNotes.get(i).getIx() - expectedNotes.get(i + 1).getIx() !=
                        actualNotes.get(i).getIx() - actualNotes.get(i + 1).getIx()) {
                    Log.d("d-bug", "dif dist");
                    return false;
                }
            }
            Log.d("d-bug", "gucci");
            return true;
        }
    }

    @Override
    public boolean sameNotes(Pattern expected, UserAnswer actual) {
        return false;
    }

    @Override
    public boolean sameNotesAnyOctave(Pattern expected, UserAnswer actual) {
        return false;
    }

    private boolean areSameKey(Pattern lhs, UserAnswer rhs) {
        return ((lhs.getNotes().get(0).getIx() % MusicTheory.TOTAL_NOTES) ==
                (rhs.getAnswer().get(0).getIx() % MusicTheory.TOTAL_NOTES));
    }
}
