package com.example.valvecontrol.ui.profile.support.viewmodel

import com.example.valvecontrol.base.viewmodel.BaseDualViewModel
import com.example.valvecontrol.ui.profile.support.viewmodel.ISupportViewModel.Event
import com.example.valvecontrol.ui.profile.support.viewmodel.ISupportViewModel.PresenterEvent

class SupportViewModel : BaseDualViewModel<Event, PresenterEvent>(), ISupportViewModel {
    override suspend fun handleEvent(event: Event) {
        TODO("Not yet implemented")
    }
}