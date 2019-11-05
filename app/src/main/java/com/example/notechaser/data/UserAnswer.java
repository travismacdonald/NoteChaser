package com.example.notechaser.data;

import com.example.keyfinder.Note;

import java.util.LinkedList;
import java.util.List;

public class UserAnswer {

    private List<Note> mAnswer;

    private int mExpectedSize;

    public UserAnswer() {
        this(-1);
    }

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

    public void setExpectedSize(int size) {
        mExpectedSize = size;
    }

    public void addNote(Note toAdd) {
        this.queue(toAdd);
        if (mAnswer.size() > mExpectedSize) {
            this.dequeue();
        }
    }

    public void clear() {
        mAnswer.clear();
    }

    public int size() {
        return mAnswer.size();
    }

    private void queue(Note toAdd) {
        mAnswer.add(toAdd);
    }

    private void dequeue() {
        mAnswer.remove(0);
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
