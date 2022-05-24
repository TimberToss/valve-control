package com.example.valvecontrol.ui.profile.main.viewmodel

import com.example.valvecontrol.base.IBaseDualViewModel
import com.example.valvecontrol.ui.profile.main.viewmodel.IProfileViewModel.Event
import com.example.valvecontrol.ui.profile.main.viewmodel.IProfileViewModel.PresenterEvent

interface IProfileViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    sealed class Event {

    }

    sealed class PresenterEvent {

    }

}