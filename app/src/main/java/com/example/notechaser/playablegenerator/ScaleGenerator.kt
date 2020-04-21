package com.example.notechaser.playablegenerator

class ScaleGenerator {

    companion object {

        public fun generateMajorMode(ix: Int): PatternTemplate {
            return generateMode(MusicTheory.MAJOR_SCALE_SEQUENCE, ix)

        }

        public fun generateMelodicMinorMode(ix: Int): PatternTemplate {
            return generateMode(MusicTheory.MELODIC_MINOR_SCALE_SEQUENCE, ix)
        }

        public fun generateHarmonicMinorMode(ix: Int): PatternTemplate {
            return generateMode(MusicTheory.HARMONIC_MINOR_SCALE_SEQUENCE, ix)
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
                toReturn.addInterval(scaleSequence[i] + MusicTheory.CHROMATIC_SCALE_SIZE - offset)
            }
            return toReturn
        }

    }

}