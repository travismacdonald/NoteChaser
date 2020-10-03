package com.example.notechaser.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.sherlock.com.sun.media.sound.SF2Soundbank
import com.example.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.example.notechaser.models.AnswerChecker
import com.example.notechaser.models.MidiPlayer
import com.example.notechaser.models.PlayablePlayer
import com.example.notechaser.models.noteprocessor.NoteProcessor
import com.example.notechaser.models.noteprocessor.NoteProcessorListener
import com.example.notechaser.models.signalprocessor.SignalProcessor
import com.example.notechaser.models.signalprocessor.SignalProcessorListener
import com.example.notechaser.playablegenerator.Playable
import com.example.notechaser.playablegenerator.PlayableGenerator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    val settings = ExerciseSetupSettings()

    lateinit var generator: PlayableGenerator

    lateinit var playablePlayer: PlayablePlayer

    private val signalProcessor = SignalProcessor()

    private val noteProcessor = NoteProcessor()

    private val answerChecker = AnswerChecker()

    val questionsAnswered = MutableLiveData(0)

    val secondsPassed = MutableLiveData<Int>()

    val currentPlayable = MutableLiveData<Playable?>()

    private var timerUpdate: Job? = null

    init {
        GlobalScope.launch {
            Timber.d("Thread started")
            val sfMidiPlayer = MidiPlayer()
            sfMidiPlayer.setPlugin(0)
            val sf = SF2Soundbank(application.assets.open("test_piano.sf2"))
            sfMidiPlayer.sf2 = sf // <-- this is the line that's taking forever ?????
            sfMidiPlayer.setPlugin(0)
            playablePlayer = PlayablePlayer(sfMidiPlayer)
            Timber.d("Thread complete")
        }

        initPitchProcessingPipeline()
    }

    fun startTimer() {
        secondsPassed.value = 0
        timerUpdate = viewModelScope.launch {
            while (true) {
                delay(1000)
                secondsPassed.value = secondsPassed.value!! + 1
            }
        }
    }

    fun stopTimer() {
        timerUpdate?.let {
            it.cancel()
        }
    }

    fun generatePlayable() {
        currentPlayable.value = generator.generatePlayable()
    }

    // Todo: come up with better function name
    fun handlePlayable(playable: Playable) {
        GlobalScope.launch {
            answerChecker.targetAnswer = playable
            answerChecker.userAnswer.clear()
            playablePlayer.playPlayable(playable)
            Timber.d("playable done")
            // Wait before listening in case the app listens to itself
            delay(250)
            Timber.d("listenenig")
            startListening()
        }
    }

    private fun initPitchProcessingPipeline() {
        signalProcessor.listener = (SignalProcessorListener { pitch, _, _ ->
            noteProcessor.onPitchDetected(pitch)
        })
        noteProcessor.listener = (object : NoteProcessorListener {
            override fun notifyNoteDetected(note: Int) {
                if (note != -1) {
                    answerChecker.addUserNote(note)
                    if (answerChecker.areAnswersSame()) {
                        signalProcessor.stop()
                        noteProcessor.clear()
                        Timber.d("correct!")
                        generatePlayable()
                    }
                }
            }
        })
    }

    private fun startListening() {
        if (signalProcessor.isRunning) {
            signalProcessor.stop()
        }
        signalProcessor.start()
    }

}