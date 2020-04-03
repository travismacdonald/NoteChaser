package com.example.notechaser.patterngenerator

internal class Pattern(template: PatternTemplate, ix: Int) {

    val notes: List<Note> = template.intervals.map { Note(it + ix) }

    val size = template.size

}