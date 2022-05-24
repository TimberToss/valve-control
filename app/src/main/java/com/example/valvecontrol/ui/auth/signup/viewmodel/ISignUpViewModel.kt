package com.example.valvecontrol.ui.auth.signup.viewmodel

import com.example.valvecontrol.base.IBaseDualViewModel
import com.example.valvecontrol.ui.auth.signup.viewmodel.ISignUpViewModel.Event
import com.example.valvecontrol.ui.auth.signup.viewmodel.ISignUpViewModel.PresenterEvent

interface ISignUpViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    sealed class Event {

    }

    sealed class PresenterEvent {

    }

}