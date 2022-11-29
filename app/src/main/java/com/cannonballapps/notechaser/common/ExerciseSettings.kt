package com.cannonballapps.notechaser.common

import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.NotePoolType
import com.cannonballapps.notechaser.musicutilities.ParentScale2
import com.cannonballapps.notechaser.musicutilities.PitchClass

data class ExerciseSettings(
    val chromaticDegrees: BooleanArray,
    val diatonicDegrees: BooleanArray,
    val modeIx: Int,
    val notePoolType: NotePoolType,
    val numQuestions: Int,
    val parentScale: ParentScale2,
    val matchOctave: Boolean,
    val playStartingPitch: Boolean,
    val playableLowerBound: Note,
    val playableUpperBound: Note,
    val questionKey: PitchClass,
    val sessionTimeLimit: Int,
    val sessionType: SessionType,
)
