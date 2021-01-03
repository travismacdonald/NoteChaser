package com.cannonballapps.notechaser.models.playablegenerator

import com.cannonballapps.notechaser.playablegenerator.Playable
import com.cannonballapps.notechaser.playablegenerator.PlayableFactory
import kotlin.random.Random


class SingleNoteGenerator(
        private val notePool: List<Int>,
        private val lowerBound: Int,
        private val upperBound: Int
) : PlayableGenerator {

    init {
        makeNotePool()
    }

    override fun generatePlayable(): Playable {
        val note = getRandomNote()
        return PlayableFactory.makePlayableFromNote(note)
    }

    private fun makeNotePool() {

    }

    private fun getRandomNote(): Int {
        val randomIx = Random.nextInt(notePool.size)
        return notePool[randomIx]
    }

}