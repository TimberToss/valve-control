package com.example.valvecontrol.data.provider

import androidx.datastore.preferences.core.stringPreferencesKey

object ValveAppDataStorePrefKeys {

    const val DATA_STORE_NAME = "VALVE_APP_DATA_STORE"

    val FIELD_USER_FIREBASE_TOKEN = stringPreferencesKey("FIELD_USER_FIREBASE_TOKEN")
}