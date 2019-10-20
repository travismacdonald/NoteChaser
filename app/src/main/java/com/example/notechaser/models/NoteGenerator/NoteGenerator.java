package com.example.notechaser.models.NoteGenerator;

import com.example.keyfinder.Voicing;

public interface NoteGenerator {

    Voicing generateNotes();

    Voicing getNotes();

    void setNoteTemplate();

}
