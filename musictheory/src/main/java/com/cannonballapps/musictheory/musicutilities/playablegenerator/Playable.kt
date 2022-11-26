package com.cannonballapps.musictheory.musicutilities.playablegenerator

import com.cannonballapps.musictheory.musicutilities.Note


data class Playable(
    val notes: List<Note>,
    val playbackType: PlaybackType
)

enum class PlaybackType {
    HARMONIC,
    MELODIC_ASCENDING,
    MELODIC_DESCENDING
}
