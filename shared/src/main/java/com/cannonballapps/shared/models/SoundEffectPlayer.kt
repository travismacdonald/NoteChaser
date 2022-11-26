package com.cannonballapps.shared.models

import android.content.Context
import android.media.MediaPlayer
import com.cannonballapps.shared.R

class SoundEffectPlayer(private val context: Context) {

    fun playCorrectSound() {
        val mp = MediaPlayer.create(context, R.raw.answer_correct)
        mp.setOnCompletionListener { mp.release() }
        mp.start()
    }

    fun playSkippedSound() {
        // TODO
    }
}
