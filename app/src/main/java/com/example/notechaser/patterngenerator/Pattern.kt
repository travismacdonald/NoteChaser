package com.example.notechaser.patterngenerator

internal class Pattern(template: PatternTemplate, ix: Int) {

    val notes: List<Note> = template.intervalsTransposed.map { Note(it + ix) }

    val size = template.size

}