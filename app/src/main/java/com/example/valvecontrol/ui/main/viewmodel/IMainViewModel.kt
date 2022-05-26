package com.example.valvecontrol.ui.main.viewmodel

import android.bluetooth.BluetoothDevice
import com.example.valvecontrol.base.viewmodel.IBaseDualViewModel
import com.example.valvecontrol.data.model.ValveSetting
import com.example.valvecontrol.ui.main.viewmodel.IMainViewModel.Event
import com.example.valvecontrol.ui.main.viewmodel.IMainViewModel.PresenterEvent
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow


interface IMainViewModel : IBaseDualViewModel<Event, PresenterEvent> {

    val firebaseUserToken: StateFlow<String?>

    val settings: StateFlow<List<ValveSetting>>

    val devices: StateFlow<List<BluetoothDevice>>

    sealed class Event {
        data class SetUser(val user: FirebaseUser) : Event()
        data class SignUp(val email: String, val password: String) : Event()
        data class Login(val email: String, val password: String) : Event()
        data class AddValveSetting(val setting: ValveSetting) : Event()
        data class UpdateValveSettings(val settings: List<ValveSetting>) : Event()
        data class SetBluetoothDevices(val devices: List<BluetoothDevice>) : Event()
        object SubscribeUser : Event()
        object GetValveSettings : Event()
        object StartScan : Event()
    }

    sealed class PresenterEvent {
        data class SignUp(val email: String, val password: String) : PresenterEvent()
        data class Login(val email: String, val password: String) : PresenterEvent()
        data class AddValveSetting(val valveSetting: ValveSetting) : PresenterEvent()
        object GetValveSettings : PresenterEvent()
        object StartScan : PresenterEvent()
    }

}