package com.cannonballapps.shared.models.signalprocessor

interface SignalProcessorListener {

    fun notifyPitchResult(pitchAsMidiNumber: Int?, probability: Float, isPitched: Boolean)
}
