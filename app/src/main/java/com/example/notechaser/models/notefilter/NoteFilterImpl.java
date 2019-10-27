package com.example.notechaser.models.notefilter;

import com.example.keyfinder.Note;

public class NoteFilterImpl implements NoteFilter {

    private static final int FILTER_LEN_MILLIS = 500;

    private Note mPrevNote;

    private long mPrevNoteInitTimeHeard;

    @Override
    public boolean isNoteValid(Note note) {
        // Same note heard
        if (note == mPrevNote && System.currentTimeMillis() - mPrevNoteInitTimeHeard > FILTER_LEN_MILLIS) {
            return true;
        }
        else if (note != mPrevNote) {
            mPrevNote = note;
            mPrevNoteInitTimeHeard = System.currentTimeMillis();
        }
        return false;
    }

    @Override
    public Note getPrevNote() {
        return mPrevNote;
    }
}
