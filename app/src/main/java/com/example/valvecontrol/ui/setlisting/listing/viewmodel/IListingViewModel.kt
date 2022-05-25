package com.example.valvecontrol.ui.setlisting.listing.viewmodel

import com.example.valvecontrol.base.IBaseDualViewModel
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

data class ValveSetting(
    val name: String,
    val segment1: Int,
    val segment2: Int,
    val segment3: Int,
    val segment4: Int,
)