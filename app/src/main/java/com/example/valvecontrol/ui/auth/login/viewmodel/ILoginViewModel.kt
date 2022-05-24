package com.example.valvecontrol.ui.auth.login.viewmodel

import com.example.valvecontrol.base.IBaseDualViewModel
import com.example.valvecontrol.ui.auth.login.viewmodel.ILoginViewModel.Event
import com.example.valvecontrol.ui.auth.login.viewmodel.ILoginViewModel.PresenterEvent

interface ILoginViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    sealed class Event {

    }

    sealed class PresenterEvent {

    }

}