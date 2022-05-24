package com.example.valvecontrol.base

import kotlinx.coroutines.flow.SharedFlow

interface IBaseViewModel<Event : Any> {

    fun sendEvent(event: Event)

    val error: SharedFlow<Throwable>

}