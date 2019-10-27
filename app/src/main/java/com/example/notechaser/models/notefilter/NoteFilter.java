package com.example.notechaser.models.notefilter;

import com.example.keyfinder.Note;

public interface NoteFilter {

    boolean isNoteValid(Note note);

    Note getPrevNote();

}
