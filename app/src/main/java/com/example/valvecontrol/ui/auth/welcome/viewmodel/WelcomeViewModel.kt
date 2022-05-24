package com.example.valvecontrol.ui.auth.welcome.viewmodel

import com.example.valvecontrol.base.BaseDualViewModel
import com.example.valvecontrol.ui.auth.welcome.viewmodel.IWelcomeViewModel.Event
import com.example.valvecontrol.ui.auth.welcome.viewmodel.IWelcomeViewModel.PresenterEvent

class WelcomeViewModel : BaseDualViewModel<Event, PresenterEvent>(), IWelcomeViewModel {

    override suspend fun handleEvent(event: Event) {
        TODO("Not yet implemented")
    }

    fun signUp() {

    }
}