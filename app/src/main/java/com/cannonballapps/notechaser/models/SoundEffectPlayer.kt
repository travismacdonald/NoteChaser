package com.cannonballapps.notechaser.models

import android.content.Context
import android.media.MediaPlayer
import com.cannonballapps.notechaser.R
import kotlin.random.Random

class SoundEffectPlayer(val context: Context) {

    private val rng = Random(System.currentTimeMillis())

    fun playCorrectSound() {
        // lmao
        if (rng.nextDouble() < 0.001) {
            val mp = MediaPlayer.create(context, R.raw.bababooey)
            mp.start()
            while (mp.isPlaying) {
                // TODO: forgive me for this, father
            }
            mp.release()
        } else {
            val mp = MediaPlayer.create(context, R.raw.answer_correct)
            mp.start()
            while (mp.isPlaying) {
                // TODO: forgive me for this, father
            }
            mp.release()
        }
        // todo: release() ?
    }

}