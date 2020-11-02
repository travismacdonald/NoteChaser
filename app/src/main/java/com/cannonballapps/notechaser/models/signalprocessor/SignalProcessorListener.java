package com.cannonballapps.notechaser.models.signalprocessor;

public interface SignalProcessorListener {

    void notifyPitchResult(int pitch, float probability, boolean isPitched);

}
