package com.ribod.kdraw.ui.main.tabs.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(private val prefs: DataStore<Preferences>) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            prefs.data.map { dataStore ->
                dataStore[intPreferencesKey("theme")] ?: 0
            }.collectLatest { theme ->
                _state.update { it.copy(currentTheme = theme) }
            }
        }
    }

    fun changeTheme(theme: Int) {
        viewModelScope.launch {
            prefs.edit { dataStore ->
                val themeKey = intPreferencesKey("theme")
                dataStore[themeKey] = theme
            }
        }
    }
}