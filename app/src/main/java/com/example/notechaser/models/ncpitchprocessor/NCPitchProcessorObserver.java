package com.example.notechaser.models.ncpitchprocessor;

public interface NCPitchProcessorObserver {

    void notifyObserver(double pitchInHz, int pitchIx);

}
