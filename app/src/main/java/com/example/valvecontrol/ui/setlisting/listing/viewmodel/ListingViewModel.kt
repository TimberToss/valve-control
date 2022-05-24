package com.example.valvecontrol.ui.setlisting.listing.viewmodel

import com.example.valvecontrol.base.BaseDualViewModel
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.IListingViewModel.Event
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.IListingViewModel.PresenterEvent

class ListingViewModel : BaseDualViewModel<Event, PresenterEvent>(), IListingViewModel {
    override suspend fun handleEvent(event: Event) {
        TODO("Not yet implemented")
    }
}