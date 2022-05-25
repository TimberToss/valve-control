package com.example.valvecontrol.data.provider

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.valvecontrol.data.provider.ValveAppDataStorePrefKeys.FIELD_USER_FIREBASE_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProvider(
    private val dataStore: DataStore<Preferences>
) : IUserProvider {

    override fun getUserFirebaseToken(): Flow<String?> =
        dataStore.data.map { prefs -> prefs[FIELD_USER_FIREBASE_TOKEN] }

    override suspend fun setUserFirebaseToken(token: String) {
        dataStore.edit { prefs -> prefs[FIELD_USER_FIREBASE_TOKEN] = token }
    }

    override suspend fun clear() {
        dataStore.edit { prefs -> prefs.remove(FIELD_USER_FIREBASE_TOKEN) }
    }
}