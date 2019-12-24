package com.example.notechaser.models.ncpitchprocessor;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.util.PitchConverter;


/**
 * Model that uses the TarsosDSP library for live pitch processing.
 */
public class NCPitchProcessorImpl implements NCPitchProcessor {

    // *********
    // CONSTANTS
    // *********

    private static final int SAMPLE_RATE = 22050;

    private static final int AUDIO_BUFFER_SIZE = 1024;

    private static final int BUFFER_OVERLAP = 0;

    // ****************
    // MEMBER VARIABLES
    // ****************

    private AudioDispatcher mDispatcher;

    private boolean mIsRunning;

    // Todo: find better way to do this.
    /**
     * Context needed for processor to run on UI thread.
     * There may be a better way to do this.
     */
    private AppCompatActivity mActivity;

    private List<NCPitchProcessorObserver> mPitchObservers;

    // ************
    // CONSTRUCTORS
    // ************

    public NCPitchProcessorImpl(AppCompatActivity activity) {
        mActivity = activity;

        mPitchObservers = new ArrayList<>();
        mDispatcher = null;
        mIsRunning = false;
    }

    // *****************
    // INTERFACE METHODS
    // *****************

    /**
     * Begin live pitch processing via microphone.
     * Update observers every time pitch is determined.
     */
    @Override
    public void start() {
        PitchDetectionHandler handler = (result, event) -> {
            mIsRunning = true;
            final float pitchInHz = result.getPitch();
            // Todo: move to thread; move off of main UI thread
            mActivity.runOnUiThread(() -> {
                int pitchAsInt = convertPitchToIx(pitchInHz);

                for (NCPitchProcessorObserver observer : mPitchObservers) {
                    observer.notifyObserver(pitchAsInt);
                }
            });
        };
        AudioProcessor pitchProcessor = new PitchProcessor(
                PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
                SAMPLE_RATE,
                AUDIO_BUFFER_SIZE,
                handler);
        mDispatcher = AudioDispatcherFactory
                .fromDefaultMicrophone(
                        SAMPLE_RATE,
                        AUDIO_BUFFER_SIZE,
                        BUFFER_OVERLAP);
        mDispatcher.addAudioProcessor(pitchProcessor);
        Thread audioThread = new Thread(mDispatcher, "Pitch Processing Thread");
        audioThread.start();
    }

    /**
     * End pitch processing.
     */
    @Override
    public void stop() {
        if (mDispatcher == null) {
            // Throw exception here. ?
            return;
        }
        mDispatcher.stop();
        mIsRunning = false;
    }

    /**
     * Add observer to be notified of pitch result.
     */
    @Override
    public void addPitchObserver(NCPitchProcessorObserver observer) {
        mPitchObservers.add(observer);
    }

    /**
     * Remove observer.
     */
    @Override
    public void removePitchObserver(NCPitchProcessorObserver observer) {
        mPitchObservers.remove(observer);
    }

    /**
     * Check if pitch processor is currently running.
     */
    @Override
    public boolean isRunning() {
        return mIsRunning;
    }

    // ***************
    // PRIVATE METHODS
    // ***************

    /**
     * Converts pitch given in hertz, to midi key.
     * (eg: 440hz -> 45)
     */
    private int convertPitchToIx(double pitchInHz) {
        if (pitchInHz == -1) {
            return -1;
        }
        return PitchConverter.hertzToMidiKey(pitchInHz);
    }
}
