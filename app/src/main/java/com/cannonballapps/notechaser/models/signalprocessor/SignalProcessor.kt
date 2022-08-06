package com.cannonballapps.notechaser.models.signalprocessor

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.util.PitchConverter
import timber.log.Timber

const val AUDIO_BUFFER_SIZE = 1024
const val BUFFER_OVERLAP = 0
const val SAMPLE_RATE = 22050 // TODO: play around with different sample rates

const val SILENCE_THRESHOLD = -75.0

class SignalProcessor {

    private lateinit var dispatcher: AudioDispatcher

    var listener: SignalProcessorListener? = null

    var isRunning = false
        private set

    fun start() {
        if (!isRunning) {
            val handler = PitchDetectionHandler { result: PitchDetectionResult, event: AudioEvent? ->
                if (isRunning) {
                    val pitchInHz = result.pitch

                    if (isSilence(pitchInHz, event)) {
                        listener?.notifyPitchResult(null, result.probability, result.isPitched)
                    } else {
                        val midiNumber = PitchConverter.hertzToMidiKey(pitchInHz.toDouble())
                        listener?.notifyPitchResult(midiNumber, result.probability, result.isPitched)
                    }
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
        Timber.d("stop() called from SignalProcessor")
        if (isRunning) {
            dispatcher.stop()
            isRunning = false
        }
    }

    private fun isSilence(pitchInHz: Float, event: AudioEvent?): Boolean {
        return pitchInHz == -1f || event?.isSilence(SILENCE_THRESHOLD) == true
    }
}
