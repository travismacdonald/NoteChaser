package com.example.notechaser.models.PitchProcessor;

public interface NCPitchProcessor {

    void start();

    void stop();

    void addPitchObserver(NCPitchProcessorObserver observer);

    void removePitchObserver(NCPitchProcessorObserver observer);

}
