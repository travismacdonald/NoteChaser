package com.example.notechaser.patterngenerator

data class Note(val ix: Int) {
    val nameSharp = MusicTheory.CHROMATIC_SCALE_SHARP[ix % MusicTheory.CHROMATIC_SCALE_SIZE]
    val nameFlat = MusicTheory.CHROMATIC_SCALE_FLAT[ix % MusicTheory.CHROMATIC_SCALE_SIZE]
}