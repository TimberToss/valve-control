package com.example.valvecontrol.ui.auth.signup.viewmodel

import com.example.valvecontrol.base.BaseDualViewModel
import com.example.valvecontrol.ui.auth.signup.viewmodel.ISignUpViewModel.Event
import com.example.valvecontrol.ui.auth.signup.viewmodel.ISignUpViewModel.PresenterEvent

class SignUpViewModel : BaseDualViewModel<Event, PresenterEvent>(), ISignUpViewModel {
    override suspend fun handleEvent(event: Event) {
        TODO("Not yet implemented")
    }
}