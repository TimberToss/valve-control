package com.example.valvecontrol.ui.profile.main.viewmodel

import com.example.valvecontrol.base.BaseDualViewModel
import com.example.valvecontrol.ui.profile.main.viewmodel.IProfileViewModel.Event
import com.example.valvecontrol.ui.profile.main.viewmodel.IProfileViewModel.PresenterEvent

class ProfileViewModel : BaseDualViewModel<Event, PresenterEvent>(), IProfileViewModel {
    override suspend fun handleEvent(event: Event) {
        TODO("Not yet implemented")
    }
}