package com.example.valvecontrol.ui.auth.signup.viewmodel

import com.example.valvecontrol.base.viewmodel.BaseDualViewModel
import com.example.valvecontrol.ui.auth.signup.viewmodel.ISignUpViewModel.Event
import com.example.valvecontrol.ui.auth.signup.viewmodel.ISignUpViewModel.PresenterEvent
import kotlinx.coroutines.flow.MutableStateFlow

class SignUpViewModel: BaseDualViewModel<Event, PresenterEvent>(), ISignUpViewModel {

    override val email = MutableStateFlow("")
    override val password = MutableStateFlow("")

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.EmailUpdate -> handleEmailUpdate(event)
            is Event.PasswordUpdate -> handlePasswordUpdate(event)
//            is Event.SignUp -> handleSignUp(event)
        }
    }

    private fun handleEmailUpdate(event: Event.EmailUpdate) {
        email.value = event.email
    }

    private fun handlePasswordUpdate(event: Event.PasswordUpdate) {
        password.value = event.password
    }

//    private fun handleSignUp(event: Event.SignUp) {
//        sendPresenterEvent(
//            PresenterEvent.SignUp(
//                email = event.email,
//                password = event.password
//            )
//        )
//    }
}