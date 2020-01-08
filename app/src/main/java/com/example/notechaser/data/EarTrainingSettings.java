package com.example.notechaser.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Data class that holds the user settings for the ear training session.
 */
public class EarTrainingSettings implements Parcelable {

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


    protected EarTrainingSettings(Parcel in) {
        mPlaybackType = (PlaybackType) in.readValue(PlaybackType.class.getClassLoader());
        byte mMatchOctaveVal = in.readByte();
        mMatchOctave = mMatchOctaveVal == 0x02 ? null : mMatchOctaveVal != 0x00;
        byte mMatchOrderVal = in.readByte();
        mMatchOrder = mMatchOrderVal == 0x02 ? null : mMatchOrderVal != 0x00;
        byte mPlayCadenceVal = in.readByte();
        mPlayCadence = mPlayCadenceVal == 0x02 ? null : mPlayCadenceVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mPlaybackType);
        if (mMatchOctave == null) {
            dest.writeByte((byte) (0x02));
        }
        else {
            dest.writeByte((byte) (mMatchOctave ? 0x01 : 0x00));
        }
        if (mMatchOrder == null) {
            dest.writeByte((byte) (0x02));
        }
        else {
            dest.writeByte((byte) (mMatchOrder ? 0x01 : 0x00));
        }
        if (mPlayCadence == null) {
            dest.writeByte((byte) (0x02));
        }
        else {
            dest.writeByte((byte) (mPlayCadence ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EarTrainingSettings> CREATOR = new Parcelable.Creator<EarTrainingSettings>() {
        @Override
        public EarTrainingSettings createFromParcel(Parcel in) {
            return new EarTrainingSettings(in);
        }

        @Override
        public EarTrainingSettings[] newArray(int size) {
            return new EarTrainingSettings[size];
        }
    };

}
