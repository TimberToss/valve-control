package com.example.valvecontrol.base.viewmodel

import kotlinx.coroutines.flow.SharedFlow

interface IBaseViewModel<Event : Any> {

    fun sendEvent(event: Event)

    val error: SharedFlow<Throwable>

}