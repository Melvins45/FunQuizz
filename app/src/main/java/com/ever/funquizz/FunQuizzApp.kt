package com.ever.funquizz     // même package que MainActivity

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.ever.funquizz.model.SoundManager

class FunQuizzApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(SoundManager)
    }
}