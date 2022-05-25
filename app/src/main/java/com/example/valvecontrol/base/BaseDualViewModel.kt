package com.example.valvecontrol.base

import com.example.valvecontrol.ui.mutableSharedFlow

abstract class BaseDualViewModel<Event : Any, PresenterEvent>
    : BaseViewModel<Event>(), IBaseDualViewModel<Event, PresenterEvent> {

    override val presenterEvent = mutableSharedFlow<PresenterEvent>()

    fun sendPresenterEvent(event: PresenterEvent) {
        presenterEvent.tryEmit(event)
    }
}