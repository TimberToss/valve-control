package com.example.valvecontrol.ui.bluetoothconnection.viewmodel

import android.bluetooth.BluetoothDevice
import com.example.valvecontrol.base.viewmodel.IBaseDualViewModel
import com.example.valvecontrol.ui.bluetoothconnection.viewmodel.IBluetoothViewModel.Event
import com.example.valvecontrol.ui.bluetoothconnection.viewmodel.IBluetoothViewModel.PresenterEvent
import kotlinx.coroutines.flow.StateFlow

interface IBluetoothViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    val bluetoothDevices: StateFlow<List<String>>

    sealed class Event {
        object StartScan : Event()
        data class ConnectDevice(val device: BluetoothDevice) : Event()
    }

    sealed class PresenterEvent {

    }

}