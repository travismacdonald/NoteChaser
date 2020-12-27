package com.cannonballapps.notechaser.viewmodels

import android.app.Application
import androidx.lifecycle.*

import cn.sherlock.com.sun.media.sound.SF2Soundbank
import com.cannonballapps.notechaser.data.NotePoolType
import com.cannonballapps.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.cannonballapps.notechaser.models.*
import com.cannonballapps.notechaser.models.noteprocessor.NoteProcessor
import com.cannonballapps.notechaser.models.noteprocessor.NoteProcessorListener
import com.cannonballapps.notechaser.models.signalprocessor.SignalProcessor
import com.cannonballapps.notechaser.models.signalprocessor.SignalProcessorListener
import com.cannonballapps.notechaser.playablegenerator.Playable
import com.cannonballapps.notechaser.playablegenerator.PlayableGenerator
import com.cannonballapps.notechaser.prefsstore.PrefsStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import timber.log.Timber


// TODO: make 2 different viewmodels: 1) one for ex configuration, and 2) for ear training session
class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    // TODO: want two types of settings: 1) global settings (theme, instr, ...), and 2) session (num questions, ..., you know what i mean)

    val settings = ExerciseSetupSettings()

    lateinit var generator: PlayableGenerator

    lateinit var playablePlayer: PlayablePlayer

    private val signalProcessor = SignalProcessor()

    private val noteProcessor = NoteProcessor()

    private val answerChecker = AnswerChecker()

    private val soundEffectPlayer = SoundEffectPlayer(application.applicationContext)

    private val ncRepository: NcRepository

    private val prefsStore = PrefsStore(application.applicationContext)

//    private val userPrefsFlow: Flow<UserPrefs>
//
//    val notePoolType: LiveData<UserPrefs>

    val questionsAnswered = MutableLiveData(0)

    val secondsPassed = MutableLiveData<Int>()

    val currentPlayable = MutableLiveData<Playable?>()

    val currentPitchDetected = MutableLiveData(-1)


    val notePoolType = prefsStore.notePoolType().asLiveData()
    val chromaticDegrees = prefsStore.chromaticDegrees().asLiveData()
    val diatonicDegrees = prefsStore.diatonicDegrees().asLiveData()


    // TODO: fix timer bug when ending session
    private var timerUpdate: Job? = null

    // TODO: make it a user parameter
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
            // TODO: extract this into a method
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



//        val dataStore: DataStore<UserPrefs> = application.applicationContext.createDataStore(
//                fileName = "user_prefs.pb",
//                serializer = UserPrefsSerializer
//        )


        ncRepository = NcRepository()


//        userPrefsFlow = ncRepository
        print("hi")

        connectPrefsStoreToRepository()
        initPitchProcessingPipeline()


    }

    fun finishSession() {
        stopTimer()
    }

    private fun connectPrefsStoreToRepository() {
        settings.numQuestions = prefsStore.numQuestions().asLiveData()

    }




    // TODO: clean up this mess
    suspend fun preloadPrefsStore() {
        runBlocking {
            var blah = prefsStore.numQuestions().first()
            print("hi")
        }
        print("hi")
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

    fun saveNotePoolType(type: NotePoolType) {
        viewModelScope.launch {
            prefsStore.saveNotePoolType(type)
        }
    }

    fun saveChromaticDegrees(degrees: BooleanArray) {
        viewModelScope.launch {
            prefsStore.saveChromaticDegrees(degrees)
        }
    }

    fun saveDiatonicDegrees(degrees: BooleanArray) {
        viewModelScope.launch {
            prefsStore.saveDiatonicDegrees(degrees)
        }
    }

    fun saveNumQuestions(numQuestions: Int) {
        viewModelScope.launch {
            prefsStore.saveNumQuestions(numQuestions)
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
                // TODO: bad variable name; rename
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