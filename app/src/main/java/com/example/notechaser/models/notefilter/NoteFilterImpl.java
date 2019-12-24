package com.example.notechaser.models.notefilter;

import com.example.keyfinder.Note;

/**
 * Model for processing pitch data.
 * This model requires a pitch to be heard continuously for a set amount of time
 * in order to be considered valid.
 */
public class NoteFilterImpl implements NoteFilter {

    // *********
    // CONSTANTS
    // *********

    private static final int FILTER_LEN_MILLIS = 250;

    // ****************
    // MEMBER VARIABLES
    // ****************

    private Note mPrevNote;

    private long mPrevNoteInitTimeHeard;

    // ************
    // CONSTRUCTORS
    // ************

    public NoteFilterImpl() {
        mPrevNote = null;
        mPrevNoteInitTimeHeard = -1;
    }

    // *****************
    // INTERFACE METHODS
    // *****************

    // Todo: simplify else statement
    /**
     * Checks if note has been consecutively heard for the required amount of milliseconds.
     */
    @Override
    public boolean isNoteValid(Note note) {
        // Different note heard
        if (!note.equals(mPrevNote)) {
            mPrevNote = note;
            mPrevNoteInitTimeHeard = System.currentTimeMillis();
        }
        else if (System.currentTimeMillis() - mPrevNoteInitTimeHeard > FILTER_LEN_MILLIS) {
            return true;
        }
        return false;
    }

    /**
     * Get the last note to checked for validation.
     */
    @Override
    public Note getPrevNote() {
        return mPrevNote;
    }
}
