package com.example.valvecontrol.ui.auth.login.viewmodel

import com.example.valvecontrol.base.viewmodel.BaseDualViewModel
import com.example.valvecontrol.ui.auth.login.viewmodel.ILoginViewModel.Event
import com.example.valvecontrol.ui.auth.login.viewmodel.ILoginViewModel.PresenterEvent
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel : BaseDualViewModel<Event, PresenterEvent>(), ILoginViewModel {

    override val email = MutableStateFlow("")
    override val password = MutableStateFlow("")

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.EmailUpdate -> handleEmailUpdate(event)
            is Event.PasswordUpdate -> handlePasswordUpdate(event)
        }
    }

    private fun handleEmailUpdate(event: Event.EmailUpdate) {
        email.value = event.email
    }

    private fun handlePasswordUpdate(event: Event.PasswordUpdate) {
        password.value = event.password
    }

}