package com.cannonballapps.notechaser.musicutilities.playablegenerator

import com.cannonballapps.notechaser.common.ExerciseSettings
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.Scale

class PlayableGeneratorFactory {

    @Deprecated("use build instead")
    fun makeNotePlayableGeneratorFromDiatonicDegrees(
        diatonicDegrees: BooleanArray,
        scale: Scale,
        key: PitchClass,
        lowerBound: Note,
        upperBound: Note,
    ): PlayableGenerator {
        val intervals: List<PitchClass> = MusicTheoryUtils.transformDiatonicDegreesToIntervals(
            diatonicDegrees,
            // todo convert everything to List<Int>
            scale.intervals.toIntArray(),
            key.value,
        ).toList().map {
            MusicTheoryUtils.CHROMATIC_PITCH_CLASSES_FLAT[it]
        }
        return NotePlayableGenerator(intervals, lowerBound, upperBound)
    }

    @Deprecated("use build instead")
    fun makeNotePlayableGeneratorFromChromaticDegrees(
        chromaticDegrees: BooleanArray,
        key: PitchClass,
        lowerBound: Note,
        upperBound: Note,
    ): PlayableGenerator {
        val intervals: List<PitchClass> = MusicTheoryUtils.transformChromaticDegreesToIntervals(
            chromaticDegrees,
            key.value,
        ).toList().map {
            MusicTheoryUtils.CHROMATIC_PITCH_CLASSES_FLAT[it]
        }
        return NotePlayableGenerator(intervals, lowerBound, upperBound)
    }

    fun build(exerciseSettings: ExerciseSettings) {

    }
}
