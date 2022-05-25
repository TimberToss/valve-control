package com.example.valvecontrol.ui.auth.login.viewmodel

import com.example.valvecontrol.base.viewmodel.IBaseDualViewModel
import com.example.valvecontrol.ui.auth.login.viewmodel.ILoginViewModel.Event
import com.example.valvecontrol.ui.auth.login.viewmodel.ILoginViewModel.PresenterEvent
import kotlinx.coroutines.flow.StateFlow

interface ILoginViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    val email: StateFlow<String>
    val password: StateFlow<String>

    sealed class Event {
        data class EmailUpdate(val email: String) : Event()
        data class PasswordUpdate(val password: String) : Event()
    }

    sealed class PresenterEvent {

    }

}