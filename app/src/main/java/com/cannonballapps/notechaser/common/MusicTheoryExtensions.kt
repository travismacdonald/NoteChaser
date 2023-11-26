package com.cannonballapps.notechaser.common

import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.playablegenerator.Playable
import com.cannonballapps.notechaser.musicutilities.playablegenerator.PlaybackType

fun PitchClass.toPlayable() =
    Playable(
        notes = listOf(Note(pitchClass = this, octave = 5)),
        playbackType = PlaybackType.HARMONIC,
    )
