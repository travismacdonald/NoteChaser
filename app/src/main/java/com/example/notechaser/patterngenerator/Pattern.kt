package com.example.notechaser.patterngenerator

internal data class Pattern(private val notes: List<Note> = arrayListOf()) {
    val size = notes.size
}