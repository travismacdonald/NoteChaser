package com.example.notechaser.models.soundpoolplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.example.notechaser.R;

public class SoundPoolPlayerImpl implements SoundPoolPlayer {

    private static final int PRIORITY = 1;

    private static final int MAX_STREAMS = 4;

    private static final int SOURCE_QUALITY = 0;

    private SoundPool mSoundPool;

    private SparseIntArray mSoundMap;

    public SoundPoolPlayerImpl(Context context) {
        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, SOURCE_QUALITY);
        mSoundMap = new SparseIntArray();

        loadSoundMap(context);
    }

    private void loadSoundMap(Context context) {
        mSoundMap.put(R.raw.answer_correct, this.mSoundPool.load(context, R.raw.answer_correct, PRIORITY));
    }

    @Override
    public void playAnswerCorrect() {
        mSoundPool.play(
                mSoundMap.get(R.raw.answer_correct),
                0.99f,
                0.99f,
                0,
                0,
                1);
    }

}
