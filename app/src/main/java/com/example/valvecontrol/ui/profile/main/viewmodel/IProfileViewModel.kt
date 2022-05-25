package com.example.valvecontrol.ui.profile.main.viewmodel

import com.example.valvecontrol.base.IBaseDualViewModel
import com.example.valvecontrol.ui.profile.main.viewmodel.IProfileViewModel.Event
import com.example.valvecontrol.ui.profile.main.viewmodel.IProfileViewModel.PresenterEvent
import kotlinx.coroutines.flow.StateFlow

interface IProfileViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    val name: StateFlow<String>

    val description: StateFlow<String>

    sealed class Event {
        data class EditAccountSettings(val name: String, val description: String) : Event()
        object ChangeThemeClicked : Event()
        object SupportClicked : Event()
        object LogOutClicked : Event()
    }

    sealed class PresenterEvent {
        data class OpenEditAccountSettingScreen(
            val name: String,
            val description: String
        ) : PresenterEvent()

        object OpenThemeScreen : PresenterEvent()
        object OpenSupportScreen : PresenterEvent()
    }

}