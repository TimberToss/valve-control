package com.example.valvecontrol.ui.auth.signup.viewmodel

import com.example.valvecontrol.base.viewmodel.IBaseDualViewModel
import com.example.valvecontrol.ui.auth.signup.viewmodel.ISignUpViewModel.Event
import com.example.valvecontrol.ui.auth.signup.viewmodel.ISignUpViewModel.PresenterEvent
import kotlinx.coroutines.flow.StateFlow

interface ISignUpViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    val email: StateFlow<String>
    val password: StateFlow<String>

    sealed class Event {
        data class EmailUpdate(val email: String) : Event()
        data class PasswordUpdate(val password: String) : Event()
//        data class SignUp(val email: String, val password: String) : Event()
    }

    sealed class PresenterEvent {
//        data class SignUp(val email: String, val password: String) : PresenterEvent()
    }

}