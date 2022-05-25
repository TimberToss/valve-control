package com.example.valvecontrol.ui.profile.editaccount.viewmodel

import com.example.valvecontrol.base.BaseDualViewModel
import com.example.valvecontrol.ui.profile.editaccount.viewmodel.IEditAccountViewModel.Event
import com.example.valvecontrol.ui.profile.editaccount.viewmodel.IEditAccountViewModel.PresenterEvent

class EditAccountViewModel
    : BaseDualViewModel<Event, PresenterEvent>(), IEditAccountViewModel {
    override suspend fun handleEvent(event: Event) {
        TODO("Not yet implemented")
    }
}