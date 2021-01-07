package com.cannonballapps.notechaser.models

import android.content.Context
import android.media.MediaPlayer
import com.cannonballapps.notechaser.R
import timber.log.Timber
import kotlin.random.Random

class SoundEffectPlayer(private val context: Context) {

    private val rng = Random(System.currentTimeMillis())

    fun playCorrectSound() {
        val mp: MediaPlayer = if (rng.nextDouble() < 0.001) {
            MediaPlayer.create(context, R.raw.bababooey)
        }
        else {
            MediaPlayer.create(context, R.raw.answer_correct)
        }
        mp.setOnCompletionListener { mp.release() }
        mp.start()
        Timber.d("exiting playCorrectSound")
    }

}