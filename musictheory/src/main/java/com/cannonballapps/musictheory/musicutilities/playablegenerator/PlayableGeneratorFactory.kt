package com.cannonballapps.musictheory.musicutilities.playablegenerator

import com.cannonballapps.musictheory.musicutilities.MusicTheoryUtils
import com.cannonballapps.musictheory.musicutilities.Note
import com.cannonballapps.musictheory.musicutilities.PitchClass
import com.cannonballapps.musictheory.musicutilities.Scale

object PlayableGeneratorFactory {

    fun makeNotePlayableGeneratorFromDiatonicDegrees(
        diatonicDegrees: BooleanArray,
        scale: Scale,
        key: PitchClass,
        lowerBound: Note,
        upperBound: Note
    ): PlayableGenerator {
        val intervals: List<PitchClass> = MusicTheoryUtils.transformDiatonicDegreesToIntervals(
            diatonicDegrees,
            scale.intervals,
            key.value
        ).toList().map {
            MusicTheoryUtils.CHROMATIC_PITCH_CLASSES_FLAT[it]
        }
        return NotePlayableGenerator(intervals, lowerBound, upperBound)
    }

    fun makeNotePlayableGeneratorFromChromaticDegrees(
        chromaticDegrees: BooleanArray,
        key: PitchClass,
        lowerBound: Note,
        upperBound: Note
    ): PlayableGenerator {
        val intervals: List<PitchClass> = MusicTheoryUtils.transformChromaticDegreesToIntervals(
            chromaticDegrees,
            key.value
        ).toList().map {
            MusicTheoryUtils.CHROMATIC_PITCH_CLASSES_FLAT[it]
        }
        return NotePlayableGenerator(intervals, lowerBound, upperBound)
    }
}
