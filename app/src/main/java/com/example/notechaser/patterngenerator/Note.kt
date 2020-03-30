package com.example.notechaser.patterngenerator

data class Note(val ix: Int) {
    val nameSharp = MusicTheory.CHROMATIC_SCALE_SHARP[ix]
    val nameFlat = MusicTheory.CHROMATIC_SCALE_FLAT[ix]
}