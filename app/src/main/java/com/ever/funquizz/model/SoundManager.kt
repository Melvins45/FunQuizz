package com.ever.funquizz.model

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

object SoundManager {
    private var bgPlayer: MediaPlayer? = null
    private var fxPlayer: MediaPlayer? = null

    /* 1. musique de fond (loop) */
    fun playBackground(context: Context, @RawRes resId: Int, volume: Float = 0.2f) {
        bgPlayer?.release()
        bgPlayer = MediaPlayer.create(context, resId).apply {
            isLooping = true
            setVolume(volume, volume)
            start()
        }
    }

    fun stopBackground() {
        bgPlayer?.stop()
        bgPlayer?.release()
        bgPlayer = null
    }

    /* 2. bruitage court */
    fun playSound(context: Context, @RawRes resId: Int, volume: Float = 0.8f) {
        fxPlayer?.release()
        fxPlayer = MediaPlayer.create(context, resId).apply {
            setVolume(volume, volume)
            start()
            setOnCompletionListener { it.release() }
        }
    }

    /* nouveau */
    val isBackgroundPlaying: Boolean
        get() = bgPlayer?.isPlaying == true

    fun setBackgroundVolume(left: Float, right: Float = left) {
        bgPlayer?.setVolume(left.coerceIn(0f, 1f), right.coerceIn(0f, 1f))
    }
}