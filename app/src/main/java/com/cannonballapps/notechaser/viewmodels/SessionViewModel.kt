package com.cannonballapps.notechaser.viewmodels


import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import cn.sherlock.com.sun.media.sound.SF2Soundbank
import com.cannonballapps.notechaser.data.ExerciseType
import com.cannonballapps.notechaser.models.MidiPlayer
import com.cannonballapps.notechaser.models.PlayablePlayer
import com.cannonballapps.notechaser.models.playablegenerator.PlayableGenerator
import com.cannonballapps.notechaser.models.playablegenerator.PlayableGeneratorFactory
import com.cannonballapps.notechaser.musicutilities.NotePoolType
import com.cannonballapps.notechaser.musicutilities.getModeAtIx
import com.cannonballapps.notechaser.playablegenerator.Playable
import com.cannonballapps.notechaser.prefsstore.PrefsStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber


class SessionViewModel @ViewModelInject constructor(
        private val prefsStore: PrefsStore,
) : ViewModel() {


    private val _curPlayable = MutableLiveData<Playable>()
    val curPlayable: LiveData<Playable>
        get() = _curPlayable

    private lateinit var generator: PlayableGenerator
    lateinit var playablePlayer: PlayablePlayer

    fun initGenerator(type: ExerciseType) {
        viewModelScope.launch {
            makePlayableGenerator(type)
        }
    }

    fun getNextPlayable() {
        val nextPlayable = generator.generatePlayable()
        _curPlayable.value = nextPlayable
        playablePlayer.playPlayable(nextPlayable)
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