package com.example.valvecontrol.ui.profile.main.viewmodel

import com.example.valvecontrol.base.BaseDualViewModel
import com.example.valvecontrol.ui.profile.main.viewmodel.IProfileViewModel.Event
import com.example.valvecontrol.ui.profile.main.viewmodel.IProfileViewModel.PresenterEvent
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileViewModel : BaseDualViewModel<Event, PresenterEvent>(), IProfileViewModel {

    override val name = MutableStateFlow("Igor Mashtakov")
    override val description = MutableStateFlow("Work as hard as possible")

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.SupportClicked -> sendPresenterEvent(PresenterEvent.OpenSupportScreen)
            is Event.ChangeThemeClicked -> sendPresenterEvent(PresenterEvent.OpenThemeScreen)
            is Event.EditAccountSettings -> sendPresenterEvent(
                PresenterEvent.OpenEditAccountSettingScreen(
                    event.name, event.description
                )
            )
            is Event.LogOutClicked -> handleLogout()
        }
    }

    private fun handleLogout() {}
}