package com.example.notechaser.patterngenerator

import java.io.Serializable

/**
 * Class that holds untransposed patterns.
 */
// todo: change serializable to parcelable
class PatternTemplate(private var notes: MutableList<Note> = arrayListOf()) : Serializable {

    var range = -1
        get() {
            if (notes.isEmpty()) -1
            // Todo: better way of writing this?
            return (notes.minBy { it.ix }?.ix ?: 0) - (notes.maxBy { it.ix }?.ix ?: 5)
        }
        private set

    var size = notes.size
        get() = notes.size
        private set

    fun addNote(note: Note) {
        notes.add(note)
    }

    fun removeNote(ix: Int) {
        notes.removeAt(ix)
    }

}