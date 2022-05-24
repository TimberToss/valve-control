package com.example.valvecontrol.ui.setlisting.editvalve.viewmodel

import com.example.valvecontrol.base.IBaseDualViewModel
import com.example.valvecontrol.ui.setlisting.editvalve.viewmodel.IEditValveViewModel.Event
import com.example.valvecontrol.ui.setlisting.editvalve.viewmodel.IEditValveViewModel.PresenterEvent

interface IEditValveViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    sealed class Event {

    }

    sealed class PresenterEvent {

    }

}