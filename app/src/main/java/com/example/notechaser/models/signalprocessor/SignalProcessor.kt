package com.example.notechaser.models.signalprocessor

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.util.PitchConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// Wrote this class in Kotlin to take advantage of coroutines
class SignalProcessor {

    private var dispatcher: AudioDispatcher? = null

    var listener: SignalProcessorListener? = null

    var isRunning = false
        private set

    fun start() {
        val handler = PitchDetectionHandler { result: PitchDetectionResult, _: AudioEvent? ->
            val pitchInHz = result.pitch
            GlobalScope.launch(Dispatchers.Main) {
                val pitchAsInt = convertPitchToIx(pitchInHz.toDouble())
                listener?.notifyPitchResult(pitchAsInt, result.probability, result.isPitched)
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
        if (dispatcher == null) {
            // Todo: throw exception here. ?
            return
        }
        dispatcher!!.stop()
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