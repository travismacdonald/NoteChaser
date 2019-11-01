package com.example.notechaser.models.notefilter;

import android.util.Log;

import com.example.keyfinder.Note;

public class NoteFilterImpl implements NoteFilter {

    private static final int FILTER_LEN_MILLIS = 300;

    private Note mPrevNote;

    private long mPrevNoteInitTimeHeard;

    public NoteFilterImpl() {
        mPrevNote = null;
        mPrevNoteInitTimeHeard = -1;
    }

    @Override
    public boolean isNoteValid(Note note) {
        // Same note heard
//        Log.d("debug", "bitch ass");
//        Log.d("debug", "len heard: " + (System.currentTimeMillis() - mPrevNoteInitTimeHeard));
        if (!note.equals(mPrevNote)) {
            Log.d("debug", "stamped");
            mPrevNote = note;
            mPrevNoteInitTimeHeard = System.currentTimeMillis();
        }

        else if (System.currentTimeMillis() - mPrevNoteInitTimeHeard > FILTER_LEN_MILLIS) {
            Log.d("debug", "valid");

//            if (note != null && mPrevNote != null) {
//                Log.d("debug", "Prev note = " + mPrevNote.getIx() + " . note = " + note.getIx());
//            }
            return true;
        }
        return false;
    }

    @Override
    public Note getPrevNote() {
        return mPrevNote;
    }
}
