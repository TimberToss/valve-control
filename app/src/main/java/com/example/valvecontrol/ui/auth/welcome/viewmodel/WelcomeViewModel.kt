package com.example.valvecontrol.ui.auth.welcome.viewmodel

import com.example.valvecontrol.base.viewmodel.BaseDualViewModel
import com.example.valvecontrol.ui.auth.welcome.viewmodel.IWelcomeViewModel.Event
import com.example.valvecontrol.ui.auth.welcome.viewmodel.IWelcomeViewModel.PresenterEvent

class WelcomeViewModel : BaseDualViewModel<Event, PresenterEvent>(), IWelcomeViewModel {

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.SignUpClicked -> sendPresenterEvent(PresenterEvent.OpenSignUpScreen)
            is Event.LoginClicked -> sendPresenterEvent(PresenterEvent.OpenLoginScreen)
        }
    }
}