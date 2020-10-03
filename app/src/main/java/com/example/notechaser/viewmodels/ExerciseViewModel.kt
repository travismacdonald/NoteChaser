package com.example.notechaser.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.sherlock.com.sun.media.sound.SF2Soundbank
import com.example.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.example.notechaser.models.MidiPlayer
import com.example.notechaser.models.PlayablePlayer
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

    val questionsAnswered = MutableLiveData(0)

    val secondsPassed = MutableLiveData<Int>()

    val currentPlayable = MutableLiveData<Playable?>()

    private var timerUpdate: Job? = null

    init {
        GlobalScope.launch {
            Timber.i("Thread started")
            val sfMidiPlayer = MidiPlayer()
            sfMidiPlayer.setPlugin(0)
            val sf = SF2Soundbank(application.assets.open("test_piano.sf2"))
            sfMidiPlayer.sf2 = sf // <-- this is the line that's taking forever ?????
            sfMidiPlayer.setPlugin(0)
            playablePlayer = PlayablePlayer(sfMidiPlayer)
            Timber.i("Thread complete")
        }
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

}