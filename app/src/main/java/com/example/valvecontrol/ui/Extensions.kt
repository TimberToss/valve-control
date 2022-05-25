package com.example.valvecontrol.ui

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.valvecontrol.data.provider.ValveAppDataStorePrefKeys.DATA_STORE_NAME
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