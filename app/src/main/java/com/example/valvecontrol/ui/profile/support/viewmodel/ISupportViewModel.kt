package com.example.valvecontrol.ui.profile.support.viewmodel

import com.example.valvecontrol.base.IBaseDualViewModel
import com.example.valvecontrol.ui.profile.support.viewmodel.ISupportViewModel.Event
import com.example.valvecontrol.ui.profile.support.viewmodel.ISupportViewModel.PresenterEvent

interface ISupportViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    sealed class Event {

    }

    sealed class PresenterEvent {

    }

}