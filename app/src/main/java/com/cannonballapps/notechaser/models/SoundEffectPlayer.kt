package com.cannonballapps.notechaser.models

import android.content.Context
import android.media.MediaPlayer
import com.cannonballapps.notechaser.R
import kotlin.random.Random

class SoundEffectPlayer(private val context: Context) {

    private val rng = Random(System.currentTimeMillis())

    fun playCorrectSound() {
        val mp = MediaPlayer.create(context, R.raw.answer_correct)
        mp.setOnCompletionListener { mp.release() }
        mp.start()
    }

}