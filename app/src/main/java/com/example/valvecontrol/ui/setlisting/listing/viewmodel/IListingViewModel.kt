package com.example.valvecontrol.ui.setlisting.listing.viewmodel

import com.example.valvecontrol.base.viewmodel.IBaseDualViewModel
import com.example.valvecontrol.data.model.ValveSetting
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.IListingViewModel.Event
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.IListingViewModel.PresenterEvent
import kotlinx.coroutines.flow.StateFlow


interface IListingViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    val settings: StateFlow<List<ValveSetting>>

    sealed class Event {
        data class EditValveSettingClicked(val setting: ValveSetting) : Event()
        data class ApplyValveSetting(val setting: ValveSetting) : Event()
    }

    sealed class PresenterEvent {
        data class OpenEditValveSettingScreen(val setting: ValveSetting) : PresenterEvent()
    }

}