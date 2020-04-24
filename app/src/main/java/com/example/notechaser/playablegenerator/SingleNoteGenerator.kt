package com.example.notechaser.playablegenerator

import androidx.lifecycle.MutableLiveData
import com.example.notechaser.utilities.MusicTheoryUtils
import java.lang.IllegalStateException
import kotlin.random.Random

class SingleNoteGenerator() : PlayableGenerator {

    /**
     * Note pool that generate will choose from to convert to patterns.
     */
    private val notePool: MutableList<Int> = mutableListOf()

    val lowerBound = MutableLiveData(36)

    val upperBound = MutableLiveData(48)

    // TODO: This class probably shouldn't be responsible for default vals
    val noteType = MutableLiveData(GeneratorNoteType.DIATONIC)

    // TODO: This class probably shouldn't be responsible for default vals
    val key = MutableLiveData(0)

    // TODO: This class probably shouldn't be responsible for default vals
    var scale = intArrayOf()

    // TODO: This class probably shouldn't be responsible for default vals
    val chromaticDegrees = MutableLiveData(booleanArrayOf(
            true, false, false, false, false, false,
            false, false, false, false, false, false
    ))

    // TODO: This class probably shouldn't be responsible for default vals
    val diatonicDegrees = MutableLiveData(booleanArrayOf(
            true, false, false, false, false, false, false
    ))

    // TODO: Make it so there has to be at least 2 true values in the array
    fun hasSelectedDegrees(): Boolean {
        return when (noteType.value) {
            GeneratorNoteType.CHROMATIC -> chromaticDegrees.value!!.contains(true)
            GeneratorNoteType.DIATONIC -> diatonicDegrees.value!!.contains(true)
            else -> throw IllegalStateException("Invalid generator note type: $noteType")
        }
    }

    /**
     * Design decision; must have at least one octave of range.
     * TODO: Come back to this later... was it a good decision?
     */
    fun hasValidRange(): Boolean {
        return (upperBound.value!! - lowerBound.value!! > MusicTheoryUtils.OCTAVE_SIZE)
    }

    fun setupGenerator() {
        if (notePool.isNotEmpty()) { notePool.clear() }
        // TODO: Make a better algorithm later
        if (noteType.value == GeneratorNoteType.DIATONIC) {

        }
        else if (noteType.value == GeneratorNoteType.CHROMATIC) {
            for (i in lowerBound.value!!..upperBound.value!!) {
                if (chromaticDegrees.value!![i + key.value!!]) {
                    notePool.add(i)
                }
            }
        }

    }

    override fun generatePlayable(): Playable {
        // TODO: should make another constructor in Playable for this situation
        return Playable(
                ChordTemplate(arrayListOf(0)),
                notePool[Random.nextInt(notePool.size)])
    }

}