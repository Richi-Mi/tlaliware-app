package com.richi_mc.tlaliwareapp.ui.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val MAC_KEY = stringPreferencesKey("mac_address")
        private val IRRIGATION_TIME_KEY = intPreferencesKey("irrigation_time")
    }

    suspend fun saveMacAddress(mac: String) {
        dataStore.edit { prefs ->
            prefs[MAC_KEY] = mac
        }
    }

    val macAddress: Flow<String?> = dataStore.data.map { prefs ->
        prefs[MAC_KEY]
    }

    suspend fun saveIrrigationTime(seconds: Int) {
        dataStore.edit { prefs ->
            prefs[IRRIGATION_TIME_KEY] = seconds
        }
    }

    val irrigationTime: Flow<Int> = dataStore.data.map { prefs ->
        prefs[IRRIGATION_TIME_KEY] ?: 5
    }
}