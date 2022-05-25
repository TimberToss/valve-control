package com.example.valvecontrol.ui.setlisting.editvalve.viewmodel

import com.example.valvecontrol.base.viewmodel.BaseDualViewModel
import com.example.valvecontrol.ui.setlisting.editvalve.viewmodel.IEditValveViewModel.Event
import com.example.valvecontrol.ui.setlisting.editvalve.viewmodel.IEditValveViewModel.PresenterEvent

class EditValveViewModel : BaseDualViewModel<Event, PresenterEvent>(), IEditValveViewModel {
    override suspend fun handleEvent(event: Event) {
        TODO("Not yet implemented")
    }
}