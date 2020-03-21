package com.example.notechaser.data;


import java.io.Serializable;

/**
 * Data class that holds the user settings for the ear training session.
 */
public class EarTrainingSettings implements Serializable {

    // Todo: refactor to have own boolean values
    public enum PlaybackType {
        ASCENDING,
        DESCENDING,
        HARMONIC,
        RANDOM
    }

    private PlaybackType mPlaybackType;

    private Boolean mMatchOctave;

    private Boolean mMatchOrder;

    private Boolean mPlayCadence;


    public EarTrainingSettings() {
        mPlaybackType = null;
        mMatchOctave = null;
        mMatchOrder = null;
        mPlayCadence = null;
    }


    public PlaybackType getPlaybackType() {
        return mPlaybackType;
    }

    public void setPlaybackAscending() {
        mPlaybackType = PlaybackType.ASCENDING;
    }

    public void setPlaybackDescending() {
        mPlaybackType = PlaybackType.DESCENDING;
    }

    public void setPlaybackHarmonic() {
        mPlaybackType = PlaybackType.HARMONIC;
    }

    public void setPlaybackRandom() {
        mPlaybackType = PlaybackType.RANDOM;
    }

    public boolean shouldMatchOctave() {
        return mMatchOctave;
    }

    public void setMatchOctave(boolean shouldMatch) {
        mMatchOctave = shouldMatch;
    }

    public boolean shouldMatchOrder() {
        return mMatchOrder;
    }

    public void setMatchOrder(boolean shouldMatch) {
        mMatchOrder = shouldMatch;
    }

    public boolean shouldPlayCadence() {
        return mPlayCadence;
    }

    public void setPlayCadence(boolean shouldPlayCadence) {
        mPlayCadence = shouldPlayCadence;
    }

}
