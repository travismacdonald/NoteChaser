package com.cannonballapps.notechaser.models.playablegenerator

import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.Scale

object PlayableGeneratorFactory {

    fun makeSingleNotePlayableGeneratorFromDiatonicDegrees(
            diatonicDegrees: BooleanArray,
            scale: Scale,
            key: PitchClass,
            lowerBound: Note,
            upperBound: Note,
    ): PlayableGenerator {

        val intervals: List<PitchClass> = MusicTheoryUtils.transformDiatonicDegreesToIntervals(
                diatonicDegrees,
                scale.intervals,
                key.value
        ).toList().map {
            MusicTheoryUtils.CHROMATIC_PITCH_CLASSES_FLAT[it]
        }
        return SingleNotePlayableGenerator(intervals, lowerBound, upperBound)
    }

    fun makeSingleNotePlayableGeneratorFromChromaticDegrees(
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
        return SingleNotePlayableGenerator(intervals, lowerBound, upperBound)
    }

}