package com.cannonballapps.notechaser.models.signalprocessor

import android.util.Log
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.util.PitchConverter
import kotlinx.coroutines.*
import timber.log.Timber

// TODO: small optimization: use a note pool please
class SignalProcessor {

    private var dispatcher: AudioDispatcher? = null

    var listener: SignalProcessorListener? = null

    var isRunning = false
        private set

    private var curNoteProcJob: Deferred<Unit?>? = null

    fun start(scope: CoroutineScope) {
        Timber.d("start called")

        val handler = PitchDetectionHandler { result: PitchDetectionResult, _: AudioEvent? ->
            runBlocking {
                val pitchInHz = result.pitch
//            GlobalScope.launch(Dispatchers.Main) {
                val pitchAsInt = convertPitchToIx(pitchInHz.toDouble())
                Log.d("THREAD-DBUG", "[0] before listener callback")
                curNoteProcJob = scope.async {
                    listener?.notifyPitchResult(pitchAsInt, result.probability, result.isPitched)
                }
                curNoteProcJob?.await()
                Log.d("THREAD-DBUG", "---[3] after listener callback")
                Log.d("THREAD-DBUG", "-----------------------------")
            }
        }
        val pitchProcessor: AudioProcessor = PitchProcessor(
                PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
                SAMPLE_RATE.toFloat(),
                AUDIO_BUFFER_SIZE,
                handler)
        dispatcher = AudioDispatcherFactory
                .fromDefaultMicrophone(
                        SAMPLE_RATE,
                        AUDIO_BUFFER_SIZE,
                        BUFFER_OVERLAP)
        dispatcher?.addAudioProcessor(pitchProcessor)
        val audioThread = Thread(dispatcher, "Pitch Processing Thread")
        audioThread.start()
        isRunning = true
    }

    fun stop() {
        Timber.d("stop called")
        dispatcher?.stop()
        dispatcher = null
        isRunning = false
    }

    private fun convertPitchToIx(pitchInHz: Double): Int {
        return if (pitchInHz == -1.0) {
            -1
        }
        else PitchConverter.hertzToMidiKey(pitchInHz)
    }

    companion object {
        const val SAMPLE_RATE = 22050
        const val AUDIO_BUFFER_SIZE = 1024
        const val BUFFER_OVERLAP = 0
    }

}