package com.cannonballapps.notechaser.viewmodels


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.cannonballapps.notechaser.data.ExerciseType
import com.cannonballapps.notechaser.models.playablegenerator.PlayableGenerator
import com.cannonballapps.notechaser.models.playablegenerator.PlayableGeneratorFactory
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.NoteFactory
import com.cannonballapps.notechaser.musicutilities.NotePoolType
import com.cannonballapps.notechaser.musicutilities.getModeAtIx
import com.cannonballapps.notechaser.playablegenerator.Playable
import com.cannonballapps.notechaser.prefsstore.PrefsStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class SessionViewModel @ViewModelInject constructor(
        private val prefsStore: PrefsStore
) : ViewModel() {

    private lateinit var generator: PlayableGenerator

    var curPlayable: Playable? = null

    fun getNextPlayable(): Playable {
        curPlayable = generator.generatePlayable()
        return curPlayable!!
    }

    fun initGenerator(type: ExerciseType) {
        viewModelScope.launch {
            makePlayableGenerator(type)
        }
    }

    private fun makePlayableGenerator(type: ExerciseType) {
        when (type) {
            ExerciseType.SINGLE_NOTE -> makeNotePlayableGenerator()
            else -> throw IllegalArgumentException(
                    "Unrecognized ExerciseType given: $type"
            )
        }
    }

    private fun makeNotePlayableGenerator() {
        viewModelScope.launch {
            val notePoolType = prefsStore.notePoolType().first()
            val key = prefsStore.questionKey().first()
            val lowerBound = prefsStore.playableLowerBound().first()
            val upperBound = prefsStore.playableUpperBound().first()

            if (notePoolType == NotePoolType.CHROMATIC) {
                val degrees = prefsStore.chromaticDegrees().first()
                generator = PlayableGeneratorFactory.makeNotePlayableGeneratorFromChromaticDegrees(
                        degrees,
                        key,
                        lowerBound,
                        upperBound
                )
            }
            else if (notePoolType == NotePoolType.DIATONIC) {
                val degrees = prefsStore.diatonicDegrees().first()
                val parentScale = prefsStore.parentScale().first()
                val modeIx = prefsStore.modeIx().first()
                val scale = parentScale.getModeAtIx(modeIx)
                generator = PlayableGeneratorFactory.makeNotePlayableGeneratorFromDiatonicDegrees(
                        degrees,
                        scale,
                        key,
                        lowerBound,
                        upperBound
                )
            }
        }
    }

}