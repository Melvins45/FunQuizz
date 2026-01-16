package com.ever.funquizz.model

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

object SoundManager : DefaultLifecycleObserver {
    private var bgPlayer: MediaPlayer? = null
    private var fxPlayer: MediaPlayer? = null

    /* quand l’app passe en arrière-plan */
    override fun onStop(owner: LifecycleOwner) {
        bgPlayer?.pause()
    }

    /* quand l’app revient */
    override fun onStart(owner: LifecycleOwner) {
        bgPlayer?.start()
    }

    /* à la destruction complète */
    override fun onDestroy(owner: LifecycleOwner) {
        stopBackground()
        fxPlayer?.release()
    }

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