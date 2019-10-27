package com.example.notechaser.activities.data;

import com.example.keyfinder.Note;

import java.util.List;

public class UserAnswer {

    private List<Note> mAnswer;

    public UserAnswer() {
        mAnswer = null;
    }

    public List<Note> getAnswer() {
        return mAnswer;
    }

    public int getAnswerSize() {
        return mAnswer.size();
    }

    public void addNote(Note toAdd) {
        mAnswer.add(toAdd);
    }

    public void clearAnswer() {
        mAnswer.clear();
    }

    public int size() {
        return mAnswer.size();
    }

}
