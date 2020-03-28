package com.example.notechaser.patterngenerator

import java.util.*


class Pattern {
    var notes: MutableList<Note>
        private set
    private var offset = 0
    private var canInterrupt = false

    // IntervalTemplate, Key
    constructor(template: PatternTemplate, key: Int) {
        // This one is pretty simple
        notes = ArrayList()
        for (ix in template.indices) {
            notes.add(Note(ix + key))
        }
    }

    fun size(): Int {
        return notes.size
    }

    fun setCanInterrupt(canInterrupt: Boolean) {
        this.canInterrupt = true
    }

    fun canInterrupt(): Boolean {
        return canInterrupt
    }

//    override fun equals(o: Any?): Boolean {
//        if (this === o) return true
//        if (o == null || javaClass != o.javaClass) return false
//        val pattern = o as Pattern
//        return notes == pattern.notes
//    }


//    override fun toString(): String {
//        val string = StringBuilder()
//        for (note in notes) {
//            string.append(note.getName())
//            string.append(note.ix)
//            string.append(' ')
//        }
//        return string.toString()
//    }

}