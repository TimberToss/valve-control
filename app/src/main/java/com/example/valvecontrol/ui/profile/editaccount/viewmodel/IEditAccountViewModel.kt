package com.example.valvecontrol.ui.profile.editaccount.viewmodel

import com.example.valvecontrol.base.IBaseDualViewModel
import com.example.valvecontrol.ui.profile.editaccount.viewmodel.IEditAccountViewModel.Event
import com.example.valvecontrol.ui.profile.editaccount.viewmodel.IEditAccountViewModel.PresenterEvent

interface IEditAccountViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    sealed class Event {

    }

    sealed class PresenterEvent {

    }

}