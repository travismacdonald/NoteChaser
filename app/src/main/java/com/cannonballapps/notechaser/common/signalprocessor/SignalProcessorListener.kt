package com.cannonballapps.notechaser.common.signalprocessor

interface SignalProcessorListener {

    fun notifyPitchResult(pitchAsMidiNumber: Int?, probability: Float, isPitched: Boolean)
}
