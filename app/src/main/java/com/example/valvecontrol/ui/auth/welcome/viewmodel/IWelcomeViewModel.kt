package com.example.valvecontrol.ui.auth.welcome.viewmodel

import com.example.valvecontrol.base.viewmodel.IBaseDualViewModel
import com.example.valvecontrol.ui.auth.welcome.viewmodel.IWelcomeViewModel.Event
import com.example.valvecontrol.ui.auth.welcome.viewmodel.IWelcomeViewModel.PresenterEvent

interface IWelcomeViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    sealed class Event {
        object SignUpClicked : Event()
        object LoginClicked : Event()
    }

    sealed class PresenterEvent {
        object OpenSignUpScreen : PresenterEvent()
        object OpenLoginScreen : PresenterEvent()
    }

}