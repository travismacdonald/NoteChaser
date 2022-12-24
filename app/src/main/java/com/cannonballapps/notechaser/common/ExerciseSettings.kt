package com.cannonballapps.notechaser.common

import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.NotePoolType
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.Scale

data class ExerciseSettings(
    val chromaticDegrees: BooleanArray,
    val diatonicDegrees: BooleanArray,
    val scale: Scale,
    val notePoolType: NotePoolType,
    val numQuestions: Int,
    val matchOctave: Boolean,
    val playStartingPitch: Boolean,
    val playableLowerBound: Note,
    val playableUpperBound: Note,
    val questionKey: PitchClass,
    val sessionTimeLimit: Int,
    val sessionType: SessionType,
)
