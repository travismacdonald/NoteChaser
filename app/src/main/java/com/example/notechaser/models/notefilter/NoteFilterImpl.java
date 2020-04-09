package com.example.notechaser.models.notefilter;


import com.example.notechaser.patterngenerator.Note;

public class NoteFilterImpl implements NoteFilter {

    private static final int FILTER_LEN_MILLIS = 250;

    private Note mPrevNote;

    private long mPrevNoteInitTimeHeard;

    public NoteFilterImpl() {
        mPrevNote = null;
        mPrevNoteInitTimeHeard = -1;
    }

    // Todo: simplify else statement
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

    @Override
    public Note getPrevNote() {
        return mPrevNote;
    }
}
