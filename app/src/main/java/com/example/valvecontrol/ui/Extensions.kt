package com.example.valvecontrol.ui

import android.os.Build
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

const val MY_TAG = "MY_TAG"

fun isApi(apiNumber: Int) = Build.VERSION.SDK_INT >= apiNumber

fun <T> mutableSharedFlow() =
    MutableSharedFlow<T>(extraBufferCapacity = 5, onBufferOverflow = BufferOverflow.DROP_OLDEST)

inline var <reified T> MutableSharedFlow<T>.value
    get() = replayCache.firstOrNull()
    set(value) {
        tryEmit(value ?: throw IllegalStateException("Try to emit null"))
    }