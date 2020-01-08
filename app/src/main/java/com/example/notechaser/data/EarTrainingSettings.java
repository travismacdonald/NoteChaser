package com.example.notechaser.data;

/**
 * Data class that holds the user settings for the ear training session.
 */
public class EarTrainingSettings {

    // ************
    // ENUM CLASSES
    // ************

    public enum PlaybackType {
        ASCENDING,
        DESCENDING,
        CHORD,
        RANDOM
    }

    // ****************
    // MEMBER VARIABLES
    // ****************

    private PlaybackType mPlaybackType;

    private Boolean mMatchOctave;

    private Boolean mMatchOrder;

    private Boolean mPlayCadence;

    // ************
    // CONSTRUCTORS
    // ************

    public EarTrainingSettings() {
        mPlaybackType = null;
        mMatchOctave = null;
        mMatchOrder = null;
        mPlayCadence = null;
    }

    // **************
    // PUBLIC METHODS
    // **************

    public PlaybackType getPlaybackType() {
        return mPlaybackType;
    }

    public void setPlaybackAscending() {
        mPlaybackType = PlaybackType.ASCENDING;
    }

    public void setPlaybackDescending() {
        mPlaybackType = PlaybackType.DESCENDING;
    }

    public void setPlaybackChord() {
        mPlaybackType = PlaybackType.CHORD;
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
