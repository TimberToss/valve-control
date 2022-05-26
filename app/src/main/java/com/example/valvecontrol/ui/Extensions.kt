package com.example.valvecontrol.ui

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.valvecontrol.data.model.ValveSetting
import com.example.valvecontrol.data.provider.ValveAppDataStorePrefKeys.DATA_STORE_NAME
import com.example.valvecontrol.ui.main.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

const val MY_TAG = "MY_TAG"

fun isApi(apiNumber: Int) = Build.VERSION.SDK_INT >= apiNumber

fun <T> mutableSharedFlow() =
    MutableSharedFlow<T>(extraBufferCapacity = 5, onBufferOverflow = BufferOverflow.DROP_OLDEST)

inline var <reified T> MutableSharedFlow<T>.value
    get() = replayCache.firstOrNull()
    set(value) {
        tryEmit(value ?: throw IllegalStateException("Try to emit null"))
    }

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

inline fun <reified T> Flow<T>.observe(
    scope: CoroutineScope,
    noinline action: suspend (T) -> Unit
) = onEach { action(it) }.launchIn(scope)

inline fun <reified T> CoroutineScope.observe(
    sharedFlow: SharedFlow<T>,
    crossinline action: suspend (T) -> Unit
) = sharedFlow.observe(this) { action(it) }

fun FirebaseUser.createUserForDocument() = hashMapOf(
    USERS_TABLE_EMAIL_FIELD to email
)

fun ValveSetting.toFirestoreSetting() = hashMapOf(
    SETTINGS_TABLE_NAME_FIELD to name,
    SETTINGS_TABLE_SEGMENT1_FIELD to segment1,
    SETTINGS_TABLE_SEGMENT2_FIELD to segment2,
    SETTINGS_TABLE_SEGMENT3_FIELD to segment3,
    SETTINGS_TABLE_SEGMENT4_FIELD to segment4
)

fun Map<String, Any>.toValveSetting() = ValveSetting(
    name = this[SETTINGS_TABLE_NAME_FIELD] as String,
    segment1 = (this[SETTINGS_TABLE_SEGMENT1_FIELD] as Long).toInt(),
    segment2 = (this[SETTINGS_TABLE_SEGMENT2_FIELD] as Long).toInt(),
    segment3 = (this[SETTINGS_TABLE_SEGMENT3_FIELD] as Long).toInt(),
    segment4 = (this[SETTINGS_TABLE_SEGMENT4_FIELD] as Long).toInt()
)