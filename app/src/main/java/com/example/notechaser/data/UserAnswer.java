package com.example.notechaser.data;

import com.example.keyfinder.Note;

import java.util.LinkedList;
import java.util.List;

public class UserAnswer {

    private List<Note> mAnswer;

    private int mExpectedSize;

    public UserAnswer(int expectedSize) {
        mAnswer = new LinkedList<>();
        mExpectedSize = expectedSize;
    }

    public List<Note> getAnswer() {
        return mAnswer;
    }

    public int getExpectedSize() {
        return mAnswer.size();
    }

    public void addNote(Note toAdd) {
        mAnswer.add(toAdd); // queue
        if (mAnswer.size() > mExpectedSize) {
            mAnswer.remove(0); // dequeue
        }
    }

    public void clear() {
        mAnswer.clear();
    }

    public int size() {
        return mAnswer.size();
    }

    @Override
    public String toString() {
        String toReturn = "";
        for (Note note : mAnswer) {
            toReturn += note.toString() + " ";
        }
        return toReturn;
    }
}
