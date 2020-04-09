package com.example.notechaser.data;



import com.example.notechaser.patterngenerator.Note;

import java.util.LinkedList;
import java.util.List;

/**
 * Class that stores data for a user answer.
 * Answers are treated like a queue, when the expected size is exceeded,
 * the head gets dequeued.
 */
public class UserAnswer {

    // ****************
    // MEMBER VARIABLES
    // ****************

    /**
     * Notes contained in user answer.
     * Used as a queue.
     */
    private List<Note> mAnswer;

    /**
     * Target number of notes in answer.
     */
    private int mExpectedSize;

    // ************
    // CONSTRUCTORS
    // ************

    public UserAnswer() {
        this(-1);
    }

    public UserAnswer(int expectedSize) {
        mAnswer = new LinkedList<>();
        mExpectedSize = expectedSize;
    }

    // **************
    // PUBLIC METHODS
    // **************

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
        // Queue
        mAnswer.add(toAdd);
        if (mAnswer.size() > mExpectedSize) {
            // Dequeue
            mAnswer.remove(0);
        }
    }

    public void clear() {
        mAnswer.clear();
    }

    public int size() {
        return mAnswer.size();
    }

    // Todo: use string builder instead of concatenation
    /**
     * Build string made of each note with spaces in between.
     */
    @Override
    public String toString() {
        String toReturn = "";
        for (Note note : mAnswer) {
            toReturn += note.toString() + " ";
        }
        return toReturn;
    }
}
