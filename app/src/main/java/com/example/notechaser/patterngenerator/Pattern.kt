package com.example.notechaser.patterngenerator

internal data class Pattern(private val notes: MutableList<Note> = mutableListOf()) {
    var size = notes.size
        get() = notes.size
        private set

    // TODO: Add functionality to add and remove notes from pattern

    // IntervalTemplate, Key
//    constructor(template: PatternTemplate, key: Int) {
//        // This one is pretty simple
//        notes = ArrayList()
//        for (ix in template.indices) {
//            notes.add(Note(ix + key))
//        }
//    }

//    fun size(): Int {
//        return notes.size
//    }

//    fun setCanInterrupt(canInterrupt: Boolean) {
//        this.canInterrupt = true
//    }

//    fun canInterrupt(): Boolean {
//        return canInterrupt
//    }

}