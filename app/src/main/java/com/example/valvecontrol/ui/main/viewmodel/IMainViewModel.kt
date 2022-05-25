package com.example.valvecontrol.ui.main.viewmodel

import com.example.valvecontrol.base.viewmodel.IBaseDualViewModel
import com.example.valvecontrol.ui.main.viewmodel.IMainViewModel.Event
import com.example.valvecontrol.ui.main.viewmodel.IMainViewModel.PresenterEvent
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow


interface IMainViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    val firebaseUserToken: StateFlow<String?>

    sealed class Event {
        data class SetUser(val user: FirebaseUser) : Event()
        data class SignUp(val email: String, val password: String) : Event()
        data class Login(val email: String, val password: String) : Event()
        object SubscribeUser : Event()
    }

    sealed class PresenterEvent {
        data class SignUp(val email: String, val password: String) : PresenterEvent()
        data class Login(val email: String, val password: String) : PresenterEvent()
    }

}