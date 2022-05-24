package com.example.valvecontrol.ui.auth.login.viewmodel

import com.example.valvecontrol.base.BaseDualViewModel
import com.example.valvecontrol.ui.auth.login.viewmodel.ILoginViewModel.Event
import com.example.valvecontrol.ui.auth.login.viewmodel.ILoginViewModel.PresenterEvent

class LoginViewModel : BaseDualViewModel<Event, PresenterEvent>(), ILoginViewModel {

    override suspend fun handleEvent(event: Event) {
        TODO("Not yet implemented")
    }

}