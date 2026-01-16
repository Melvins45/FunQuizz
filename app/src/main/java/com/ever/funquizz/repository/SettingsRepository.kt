package com.ever.funquizz.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ever.funquizz.model.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

private val MUSIC_KEY = floatPreferencesKey("music_volume")
private val FX_KEY    = floatPreferencesKey("fx_volume")

class SettingsRepository(private val context: Context) {

    /* FLOW lecture */
    val musicVolume: Flow<Float> = context.dataStore.data.map { it[MUSIC_KEY] ?: 0.7f }
    val fxVolume:    Flow<Float> = context.dataStore.data.map { it[FX_KEY]    ?: 0.8f }
    /* Thème */
    private val THEME_KEY = stringPreferencesKey("theme") // "SYSTEM", "LIGHT", "DARK"

    val theme: Flow<Theme> = context.dataStore.data
        .map { Theme.valueOf(it[THEME_KEY] ?: "SYSTEM") }

    suspend fun setTheme(theme: Theme) {
        context.dataStore.edit { it[THEME_KEY] = theme.name }
    }

    /* écriture */
    suspend fun setMusic(volume: Float) = context.dataStore.edit { it[MUSIC_KEY] = volume.coerceIn(0f,1f) }
    suspend fun setFx(volume: Float)    = context.dataStore.edit { it[FX_KEY]    = volume.coerceIn(0f,1f) }
}