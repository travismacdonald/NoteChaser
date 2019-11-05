package com.example.notechaser.data;

import com.example.keyfinder.HarmonicMinorMode;
import com.example.keyfinder.MajorMode;
import com.example.keyfinder.MelodicMinorMode;
import com.example.keyfinder.MusicTheory;


// Todo: harmonic minor

public class ModeCollection {

    private final MajorMode[] mMajorModes;

    private final MelodicMinorMode[] mMelodicMinorModes;

    private final HarmonicMinorMode[] mHarmonicMinorModes;

    public ModeCollection() {
        mMajorModes = new MajorMode[MusicTheory.DIATONIC_SCALE_SIZE];
        mMelodicMinorModes = new MelodicMinorMode[MusicTheory.DIATONIC_SCALE_SIZE];
        mHarmonicMinorModes = new HarmonicMinorMode[MusicTheory.DIATONIC_SCALE_SIZE];

        for (int i = 0; i < MusicTheory.DIATONIC_SCALE_SIZE; i++) {
            mMajorModes[i] = new MajorMode(i);
            mMelodicMinorModes[i] = new MelodicMinorMode(i);
            mHarmonicMinorModes[i] = new HarmonicMinorMode(i);
        }
    }

    // Todo: some way to handle invalid indices given

    public MajorMode getMajorMode(int modeIx) {
        return mMajorModes[modeIx];
    }

    public MelodicMinorMode getMelodicMinorMode(int modeIx) {
        return mMelodicMinorModes[modeIx];
    }

    public HarmonicMinorMode getHarmonicMode(int modeIx) {
        return mHarmonicMinorModes[modeIx];
    }

}
