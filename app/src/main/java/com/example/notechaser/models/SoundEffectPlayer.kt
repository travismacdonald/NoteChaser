package com.example.notechaser.models

import android.content.Context
import android.media.MediaPlayer
import com.example.notechaser.R
import kotlin.random.Random

class SoundEffectPlayer(val context: Context) {

    val rng = Random(System.currentTimeMillis())

    fun playCorrectSound() {
        if (rng.nextDouble() < 0.0001) {
            val mp = MediaPlayer.create(context, R.raw.bababooey)
            mp.start()
            mp.setOnCompletionListener { mediaPlayer -> mediaPlayer.release() }
        } else {
            val mp = MediaPlayer.create(context, R.raw.answer_correct)
            mp.start()
            mp.setOnCompletionListener { mediaPlayer -> mediaPlayer.release() }
        }
        // todo: release() ?
    }


//    fun playFinishedSound() {
//        val mp = MediaPlayer.create(context, R.raw.beep)
//        mp.start()
//        mp.setOnCompletionListener { mediaPlayer -> mediaPlayer.release() }
//    }

//    fun playMetronomeClack() {
//        if (metronomeClack.isPlaying) {
//            Timber.i("seek called")
//            metronomeClack.seekTo(0)
//        }
//        else {
//            Timber.i("start called")
//            metronomeClack.start()
//        }
//
//    }

}