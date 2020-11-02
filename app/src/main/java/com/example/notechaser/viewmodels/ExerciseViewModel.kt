package com.example.notechaser.viewmodels

import android.app.Application
import androidx.datastore.DataStore

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.sherlock.com.sun.media.sound.SF2Soundbank
import com.example.notechaser.R
import com.example.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.example.notechaser.models.*
import com.example.notechaser.models.noteprocessor.NoteProcessor
import com.example.notechaser.models.noteprocessor.NoteProcessorListener
import com.example.notechaser.models.signalprocessor.SignalProcessor
import com.example.notechaser.models.signalprocessor.SignalProcessorListener
import com.example.notechaser.playablegenerator.Playable
import com.example.notechaser.playablegenerator.PlayableGenerator
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.prefs.Preferences


class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    val settings = ExerciseSetupSettings()

    lateinit var generator: PlayableGenerator

    lateinit var playablePlayer: PlayablePlayer

    private val signalProcessor = SignalProcessor()

    private val noteProcessor = NoteProcessor()

    private val answerChecker = AnswerChecker()

    private val soundEffectPlayer = SoundEffectPlayer(application.applicationContext)

//    private val ncRepository: NCRepository

    val questionsAnswered = MutableLiveData(0)

    val secondsPassed = MutableLiveData<Int>()

    val currentPlayable = MutableLiveData<Playable?>()

    val currentPitchDetected = MutableLiveData(-1)

    // TODO: fix timer bug when ending session
    private var timerUpdate: Job? = null

    // The number of millis --- after initially detecting silence ---
    // the app waits before replaying the current playable.
    private var silenceThreshold: Long = 2000

    private var replayPlayable: Job? = null

    // TODO: turn into user parameter
    private var pauseAfterPlayableMillis: Long = 250

    // TODO: turn into user parameter
    private var pauseAfterCorrectSoundMillis: Long = 500

    init {
        viewModelScope.launch {
            Timber.d("Midi setup started")
            val sfMidiPlayer = MidiPlayer()
            sfMidiPlayer.setPlugin(0)
            // TODO: look into fixing this error?
            val sf = SF2Soundbank(application.assets.open("test_piano.sf2"))
            // this is the line that's taking forever for certain sf2 files ?????
            sfMidiPlayer.sf2 = sf
            sfMidiPlayer.setPlugin(0)
            playablePlayer = PlayablePlayer(sfMidiPlayer)
            Timber.d("Midi setup complete")
        }
//        val dataStore: DataStore<Preferences> = application.applicationContext.createDataStore(
//                name = application.applicationContext.getString(R.string.dataStore_name)
//        )
//        ncRepository = NCRepository(dataStore)
        initPitchProcessingPipeline()
    }

    fun finishSession() {
        stopTimer()
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
        timerUpdate?.cancel()
    }

    fun generatePlayable() {
        currentPlayable.value = generator.generatePlayable()
    }

    // Todo: come up with better function name
    fun handlePlayable(playable: Playable) {
        viewModelScope.launch {
            answerChecker.targetAnswer = playable
            answerChecker.userAnswer.clear()
            noteProcessor.clear()
            playablePlayer.playPlayable(playable)
            // Wait before beginning pitch processing to avoid
            // picking up own output as input
            delay(pauseAfterPlayableMillis)
            startListening()
        }
    }

    private fun initPitchProcessingPipeline() {
        signalProcessor.listener = (SignalProcessorListener { note, _, _ ->
            currentPitchDetected.value = note
            Timber.d(" note: $note")
            noteProcessor.onPitchDetected(note)
        })
        noteProcessor.listener = (object : NoteProcessorListener {
            override fun notifyNoteDetected(note: Int) {
//                currentPitchDetected.value = note
                // Actual note detected (because -1 denotes silence)
                if (note != -1) {
                    answerChecker.addUserNote(note)
                    if (answerChecker.areAnswersSame()) {
                        signalProcessor.stop()
                        noteProcessor.clear()
                        viewModelScope.launch {
                            soundEffectPlayer.playCorrectSound()
                            delay(pauseAfterCorrectSoundMillis)
                            questionsAnswered.value = questionsAnswered.value!! + 1
                        }
                    }
                }
                // Silence Detected
                else {
                    replayPlayable = viewModelScope.launch {
                        delay(silenceThreshold)
                        signalProcessor.stop()
                        handlePlayable(currentPlayable.value!!)
                    }
                }
            }

            override fun notifyNoteUndetected(note: Int) {
//                currentPitchDetected.value = -1
                // Doesn't matter if silence or not is undetected;
                replayPlayable?.cancel()
            }
        })
    }

    private fun startListening() {
        if (signalProcessor.isRunning) {
            signalProcessor.stop()
        }
        signalProcessor.start(viewModelScope)
    }

}