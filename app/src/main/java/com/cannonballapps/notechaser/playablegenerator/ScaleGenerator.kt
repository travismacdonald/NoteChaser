package com.cannonballapps.notechaser.playablegenerator

import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils

class ScaleGenerator {

    companion object {

        public fun generateMajorMode(ix: Int): PatternTemplate {
            return generateMode(MusicTheoryUtils.MAJOR_SCALE_INTERVALS, ix)

        }

        public fun generateMelodicMinorMode(ix: Int): PatternTemplate {
            return generateMode(MusicTheoryUtils.MELODIC_MINOR_SCALE_INTERVALS, ix)
        }

        public fun generateHarmonicMinorMode(ix: Int): PatternTemplate {
            return generateMode(MusicTheoryUtils.HARMONIC_MINOR_SCALE_INTERVALS, ix)
        }

        /**
         * Method generates untransposed scales as PatternTemplates.
         * e.g. Given Major Scale [0 2 4 5 7 9 11], and ix = 2 (Phrygian),
         * will generate PatternTemplate containing [0 1 3 5 7 8 10].
         */
        private fun generateMode(scaleSequence: IntArray, ix: Int): PatternTemplate {
            val offset = scaleSequence[ix]
            val toReturn = PatternTemplate()
            for (i in ix until scaleSequence.size) {
                toReturn.addInterval(scaleSequence[i] - offset)
            }
            for (i in 0 until ix) {
                toReturn.addInterval(scaleSequence[i] + MusicTheoryUtils.OCTAVE_SIZE - offset)
            }
            return toReturn
        }

    }

}