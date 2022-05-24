package com.example.valvecontrol.base

import kotlinx.coroutines.flow.SharedFlow

interface IBaseDualViewModel<Event : Any, PresenterEvent> : IBaseViewModel<Event> {

    val presenterEvent: SharedFlow<PresenterEvent>

}