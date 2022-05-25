package com.example.valvecontrol.ui.setlisting.listing.viewmodel

import com.example.valvecontrol.base.viewmodel.BaseDualViewModel
import com.example.valvecontrol.data.model.ValveSetting
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.IListingViewModel.Event
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.IListingViewModel.PresenterEvent
import kotlinx.coroutines.flow.MutableStateFlow

class ListingViewModel : BaseDualViewModel<Event, PresenterEvent>(), IListingViewModel {

    override val settings = MutableStateFlow(
        listOf(
            ValveSetting(
                "Город", 124, 1548, 4506, 6453
            ),
            ValveSetting(
                "Бездорожье", 848, 2000, 3820, 10243
            ),
            ValveSetting(
                "Лес", 3, 2344, 3001, 8432
            ),
            ValveSetting(
                "Холмы", 45, 1002, 5999, 6001
            )
        )
    )

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.ApplyValveSetting -> handleApplyValveSetting(event.setting)
            is Event.EditValveSettingClicked -> sendPresenterEvent(
                PresenterEvent.OpenEditValveSettingScreen(
                    event.setting
                )
            )
        }
    }

    private fun handleApplyValveSetting(setting: ValveSetting) {

    }
}