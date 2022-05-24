package com.example.valvecontrol.ui.bluetoothconnection.viewmodel

import com.example.valvecontrol.base.IBaseDualViewModel
import com.example.valvecontrol.ui.bluetoothconnection.viewmodel.IBluetoothViewModel.Event
import com.example.valvecontrol.ui.bluetoothconnection.viewmodel.IBluetoothViewModel.PresenterEvent

interface IBluetoothViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    sealed class Event {

    }

    sealed class PresenterEvent {

    }

}