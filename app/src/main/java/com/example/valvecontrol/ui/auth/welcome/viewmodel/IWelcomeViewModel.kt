package com.example.valvecontrol.ui.auth.welcome.viewmodel

import com.example.valvecontrol.base.IBaseDualViewModel
import com.example.valvecontrol.ui.auth.welcome.viewmodel.IWelcomeViewModel.Event
import com.example.valvecontrol.ui.auth.welcome.viewmodel.IWelcomeViewModel.PresenterEvent

interface IWelcomeViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    sealed class Event {
        object SignUp : Event()
    }

    sealed class PresenterEvent {

    }

}