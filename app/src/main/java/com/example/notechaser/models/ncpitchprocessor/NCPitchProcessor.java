package com.example.notechaser.models.ncpitchprocessor;

public interface NCPitchProcessor {

    void start();

    void stop();

    void addPitchObserver(NCPitchProcessorObserver observer);

    void removePitchObserver(NCPitchProcessorObserver observer);

    boolean isRunning();

}
