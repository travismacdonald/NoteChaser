package com.cannonballapps.notechaser.models.signalprocessor

interface SignalProcessorListener {

    fun notifyPitchResult(pitchAsMidiNumber: Int?, probability: Float, isPitched: Boolean)

}