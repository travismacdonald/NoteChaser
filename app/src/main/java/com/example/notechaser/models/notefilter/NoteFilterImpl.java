package com.example.notechaser.models.notefilter;

import android.util.Log;

import com.example.keyfinder.Note;

public class NoteFilterImpl implements NoteFilter {

    private static final int FILTER_LEN_MILLIS = 250;

    private Note mPrevNote;

    private long mPrevNoteInitTimeHeard;

    public NoteFilterImpl() {
        mPrevNote = null;
        mPrevNoteInitTimeHeard = -1;
    }

    @Override
    public boolean isNoteValid(Note note) {
        // Same note heard
        if (!note.equals(mPrevNote)) {
            mPrevNote = note;
            mPrevNoteInitTimeHeard = System.currentTimeMillis();
        }

        else if (System.currentTimeMillis() - mPrevNoteInitTimeHeard > FILTER_LEN_MILLIS) {
            return true;
        }
        return false;
    }

    @Override
    public Note getPrevNote() {
        return mPrevNote;
    }
}
