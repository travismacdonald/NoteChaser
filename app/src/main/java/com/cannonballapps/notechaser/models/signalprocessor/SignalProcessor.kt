package com.cannonballapps.notechaser.models.signalprocessor

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.util.PitchConverter

const val SAMPLE_RATE = 22050
const val AUDIO_BUFFER_SIZE = 1024
const val BUFFER_OVERLAP = 0

class SignalProcessor {

    private lateinit var dispatcher: AudioDispatcher

    var listener: SignalProcessorListener? = null

    var isRunning = false
        private set

    fun start() {
        if (!isRunning) {
            val handler = PitchDetectionHandler { result: PitchDetectionResult, event: AudioEvent? ->
                // TODO: experiment with the dB level from AudioEvent
                if (isRunning) {
                    val pitchInHz = result.pitch
                    val pitchMidiNumber = pitchToMidiNumber(pitchInHz.toDouble())
                    listener?.notifyPitchResult(pitchMidiNumber, result.probability, result.isPitched)
                }
            }
            val pitchProcessor: AudioProcessor = PitchProcessor(
                    PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
                    SAMPLE_RATE.toFloat(),
                    AUDIO_BUFFER_SIZE,
                    handler
            )
            dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(
                    SAMPLE_RATE,
                    AUDIO_BUFFER_SIZE,
                    BUFFER_OVERLAP
            )
            dispatcher.addAudioProcessor(pitchProcessor)
            isRunning = true
            dispatcher.run()
        }

    }

    fun stop() {
        dispatcher.stop()
        isRunning = false
    }

    private fun pitchToMidiNumber(pitchInHz: Double): Int {
        return if (pitchInHz == -1.0) {
            -1
        }
        else PitchConverter.hertzToMidiKey(pitchInHz)
    }


}