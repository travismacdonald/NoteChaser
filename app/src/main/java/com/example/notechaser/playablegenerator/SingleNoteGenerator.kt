package com.example.notechaser.playablegenerator

import androidx.lifecycle.MutableLiveData
import com.example.notechaser.utilities.MusicTheoryUtils
import java.lang.IllegalStateException
import kotlin.random.Random

// TODO: This class probably shouldn't be responsible for default values (multiple fixes here)
//       SOLUTION: make some sort of injector function to take care of this
class SingleNoteGenerator() : PlayableGenerator {

    /**
     * Note pool that generate will choose from to convert to patterns.
     */
    private val notePool: MutableList<Int> = mutableListOf()

    val lowerBound = MutableLiveData(36)

    val upperBound = MutableLiveData(48)

    val noteType = MutableLiveData(GeneratorNoteType.DIATONIC)

    val questionKey = MutableLiveData(0)

    // TODO: extract hardcoded parent scale
    val parentScale = MutableLiveData(ParentScale("Major", MusicTheoryUtils.MAJOR_SCALE_SEQUENCE))

    // TODO: combine parent scale and mode into single parameter
    val mode = MutableLiveData(0)

    var scale = intArrayOf()

    val chromaticDegrees = MutableLiveData(booleanArrayOf(
            true, false, false, false, false, false,
            false, false, false, false, false, false
    ))

    val diatonicDegrees = MutableLiveData(booleanArrayOf(
            true, false, false, false, false, false, false
    ))

    val selectAllChromatic = MutableLiveData(false)

    val selectAllDiatonic = MutableLiveData(false)

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
                if (chromaticDegrees.value!![i + questionKey.value!!]) {
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