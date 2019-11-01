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

public class NCPitchProcessorImpl implements NCPitchProcessor {

    private static final int SAMPLE_RATE = 22050;

    private static final int AUDIO_BUFFER_SIZE = 1024;

    private static final int BUFFER_OVERLAP = 0;

    private AudioDispatcher mDispatcher;

    /**
     * Context needed for processor to run on UI thread.
     * There may be a better way to do this.
     */
    private AppCompatActivity mActivity;

    private List<NCPitchProcessorObserver> mPitchObservers;

    public NCPitchProcessorImpl(AppCompatActivity activity) {
        mActivity = activity;

        mPitchObservers = new ArrayList<>();
        mDispatcher = null;
    }

    @Override
    public void start() {
        PitchDetectionHandler handler = (result, event) -> {
            final float pitchInHz = result.getPitch();
            mActivity.runOnUiThread(() -> {
                int pitchAsInt = convertPitchToIx(pitchInHz);
                for (NCPitchProcessorObserver observer : mPitchObservers) {
                    observer.handlePitchResult(pitchAsInt);
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

    @Override
    public void stop() {
        if (mDispatcher == null) {
            // Throw exception here. ?
            return;
        }
        mDispatcher.stop();
        mDispatcher = null;
    }

    @Override
    public void addPitchObserver(NCPitchProcessorObserver observer) {
        mPitchObservers.add(observer);
    }

    @Override
    public void removePitchObserver(NCPitchProcessorObserver observer) {
        mPitchObservers.remove(observer);
    }

    @Override
    public boolean isRunning() {
        return mDispatcher == null;
    }

    private int convertPitchToIx(double pitchInHz) {
        if (pitchInHz == -1) {
            return -1;
        }
        return PitchConverter.hertzToMidiKey(pitchInHz);
    }
}
