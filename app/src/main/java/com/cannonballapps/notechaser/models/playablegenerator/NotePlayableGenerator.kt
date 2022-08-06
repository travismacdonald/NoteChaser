package com.cannonballapps.notechaser.models.playablegenerator

import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.playablegenerator.Playable
import com.cannonballapps.notechaser.playablegenerator.PlayableFactory
import kotlin.random.Random

class NotePlayableGenerator(
    private val pitchClassPool: List<PitchClass>,
    private val lowerBound: Note,
    private val upperBound: Note
) : PlayableGenerator {

    override fun generatePlayable(): Playable {
        return getRandomPlayable()
    }

    private fun getRandomPlayable(): Playable {
        val randPitchClass = getRandomPitchClass()
        val randNote = getRandomNoteWithinBounds(randPitchClass)
        return PlayableFactory.makePlayableFromNote(randNote)
    }

    private fun getRandomPitchClass(): PitchClass {
        val randomIx = Random.nextInt(pitchClassPool.size)
        return pitchClassPool[randomIx]
    }

    private fun getRandomNoteWithinBounds(pitchClass: PitchClass): Note {
        val lowestOccurrence = MusicTheoryUtils.getLowestPitchClassOccurrenceBetweenBoundsOrNull(
            pitchClass,
            lowerBound,
            upperBound
        )
        val numOccurrences = MusicTheoryUtils.getNumberOfPitchClassOccurrencesBetweenBounds(
            pitchClass,
            lowerBound,
            upperBound
        )
        val numOctavesOffset = Random.nextInt(numOccurrences)
        val midiNum = lowestOccurrence!!.midiNumber + (numOctavesOffset * MusicTheoryUtils.OCTAVE_SIZE)

        return Note(midiNum)
    }
}
