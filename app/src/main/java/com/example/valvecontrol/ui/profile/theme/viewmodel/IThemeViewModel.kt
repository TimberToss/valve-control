package com.example.valvecontrol.ui.profile.theme.viewmodel

import com.example.valvecontrol.base.viewmodel.IBaseDualViewModel
import com.example.valvecontrol.ui.profile.theme.viewmodel.IThemeViewModel.Event
import com.example.valvecontrol.ui.profile.theme.viewmodel.IThemeViewModel.PresenterEvent

interface IThemeViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    sealed class Event {

    }

    sealed class PresenterEvent {

    }

}