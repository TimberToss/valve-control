package com.example.valvecontrol.ui.bluetoothconnection.viewmodel

import com.example.valvecontrol.base.BaseDualViewModel
import com.example.valvecontrol.ui.bluetoothconnection.viewmodel.IBluetoothViewModel.Event
import com.example.valvecontrol.ui.bluetoothconnection.viewmodel.IBluetoothViewModel.PresenterEvent

class BluetoothViewModel : BaseDualViewModel<Event, PresenterEvent>(), IBluetoothViewModel {
    override suspend fun handleEvent(event: Event) {
        TODO("Not yet implemented")
    }
}