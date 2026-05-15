package com.karina0088.studenttasktracker.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class ThemePreferences(private val context: Context) {

    companion object {
        private val DARK_MODE = booleanPreferencesKey("dark_mode")
        private val IS_GRID = booleanPreferencesKey("is_grid")
    }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE] ?: false
    }

    suspend fun saveDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = isDarkMode
        }
    }

    val isGridFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_GRID] ?: false
    }

    suspend fun saveIsGrid(isGrid: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_GRID] = isGrid
        }
    }
}