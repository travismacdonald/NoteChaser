package com.example.notechaser.playablegenerator

import androidx.lifecycle.MutableLiveData
import com.example.notechaser.utilities.MusicTheoryUtils
import timber.log.Timber
import java.lang.IllegalStateException
import kotlin.random.Random

// TODO: This class probably shouldn't be responsible for default values (multiple fixes here)
//       SOLUTION: make some sort of injector function to take care of this
class SingleNoteGenerator() : PlayableGenerator {

    /**
     * Note pool that generate will choose from to convert to patterns.
     */
    private lateinit var notePool: MutableList<Int>

    val lowerBound = MutableLiveData(36)

    val upperBound = MutableLiveData(48)

    // TODO: change this later when an algorithm is figured out
    val minRange = MutableLiveData(11)

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

        // TODO: Make a better algorithm later
        if (noteType.value == GeneratorNoteType.DIATONIC) {

        }
        // TODO: i think this algorithm works
        else if (noteType.value == GeneratorNoteType.CHROMATIC) {
            val intervals = MusicTheoryUtils.transformChromaticDegreesToIntervals(chromaticDegrees.value!!, questionKey.value!!)
            val lower = lowerBound.value!!
            notePool = createNotePool(intervals, lowerBound.value!!, upperBound.value!!)
            Timber.d("intvls: $intervals")
            Timber.d("bounds: ${lowerBound.value} to ${upperBound.value}")
            Timber.d("notePool: ${notePool.toString()}")
            Timber.d("names: ${notePool.map { MusicTheoryUtils.ixToName(it) }}")
        }
    }

    override fun generatePlayable(): Playable {
        // TODO: should make another constructor in Playable for this situation
        return Playable(
                ChordTemplate(arrayListOf(0)),
                notePool[Random.nextInt(notePool.size)])
    }

    private fun createNotePool(intervals: IntArray, lower: Int, upper: Int): MutableList<Int> {
        val toReturn = arrayListOf<Int>()
        val startInterval = lower % MusicTheoryUtils.OCTAVE_SIZE
        val something = lower / MusicTheoryUtils.OCTAVE_SIZE
        var ix = (something * intervals.size)
        val greaterThanStart = intervals.filter { it >= startInterval }
        if (greaterThanStart.isNotEmpty()) {
            // TODO: can probably make this line better
            ix += intervals.indexOf(greaterThanStart[0])
        }
        else {
            ix += intervals.size
        }
        var interval = intervals[ix % intervals.size] + (MusicTheoryUtils.OCTAVE_SIZE * (ix / intervals.size))
        // Inclusive upper bound
        while (interval <= upper) {
            toReturn.add(interval)
            ix++
            interval = intervals[ix % intervals.size] + (MusicTheoryUtils.OCTAVE_SIZE * (ix / intervals.size))
        }
        return toReturn
    }

}