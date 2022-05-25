package com.example.valvecontrol.base.viewmodel

import kotlinx.coroutines.flow.SharedFlow

interface IBaseDualViewModel<Event : Any, PresenterEvent> : IBaseViewModel<Event> {

    val presenterEvent: SharedFlow<PresenterEvent>

}