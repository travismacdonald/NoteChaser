package com.cannonballapps.notechaser.playablegenerator

import com.cannonballapps.notechaser.musicutilities.Note

data class Playable(
        val notes: List<Note>,
        val playbackType: PlaybackType
)

enum class PlaybackType {
    HARMONIC,
    MELODIC_ASCENDING,
    MELODIC_DESCENDING
}
