package com.example.valvecontrol.ui.setlisting.listing.viewmodel

import com.example.valvecontrol.base.IBaseDualViewModel
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.IListingViewModel.Event
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.IListingViewModel.PresenterEvent


interface IListingViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    sealed class Event {

    }

    sealed class PresenterEvent {

    }

}