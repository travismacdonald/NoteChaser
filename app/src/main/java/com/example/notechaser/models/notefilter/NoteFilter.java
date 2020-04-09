package com.example.notechaser.models.notefilter;


import com.example.notechaser.patterngenerator.Note;

public interface NoteFilter {

    boolean isNoteValid(Note note);

    Note getPrevNote();

}
