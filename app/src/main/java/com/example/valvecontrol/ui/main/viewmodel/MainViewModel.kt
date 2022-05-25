package com.example.valvecontrol.ui.main.viewmodel

import com.example.valvecontrol.base.viewmodel.BaseDualViewModel
import com.example.valvecontrol.data.provider.IUserProvider
import com.example.valvecontrol.data.provider.UserProvider
import com.example.valvecontrol.ui.auth.signup.viewmodel.ISignUpViewModel
import com.example.valvecontrol.ui.main.viewmodel.IMainViewModel.Event
import com.example.valvecontrol.ui.main.viewmodel.IMainViewModel.PresenterEvent
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel(
    private val userProvider: IUserProvider
) : BaseDualViewModel<Event, PresenterEvent>(), IMainViewModel {

    override val firebaseUserToken = MutableStateFlow<String?>(null)

    init {
        sendEvent(Event.SubscribeUser)
    }

    override suspend fun handleEvent(event: Event) {
        when(event) {
            is Event.SetUser -> longRunning { handleSetUser(event.user) }
            is Event.SubscribeUser -> longRunning(::handleSubscribeUser)
            is Event.SignUp -> handleSignUp(event)
            is Event.Login -> handleLogin(event)
        }
    }

    private suspend fun handleSetUser(user: FirebaseUser) {
        userProvider.setUserFirebaseToken(user.uid)
    }

    private suspend fun handleSubscribeUser() {
        userProvider.getUserFirebaseToken().collect {
            firebaseUserToken.value = it
        }
    }

    private fun handleSignUp(event: Event.SignUp) {
        sendPresenterEvent(
            PresenterEvent.SignUp(
                email = event.email,
                password = event.password
            )
        )
    }

    private fun handleLogin(event: Event.Login) {
        sendPresenterEvent(
            PresenterEvent.Login(
                email = event.email,
                password = event.password
            )
        )
    }

}