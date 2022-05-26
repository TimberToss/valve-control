package com.example.valvecontrol.ui.main.viewmodel

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import android.util.Log
import com.example.valvecontrol.base.viewmodel.BaseDualViewModel
import com.example.valvecontrol.data.model.ValveSetting
import com.example.valvecontrol.data.provider.IUserProvider
import com.example.valvecontrol.ui.MY_TAG
import com.example.valvecontrol.ui.logGattServices
import com.example.valvecontrol.ui.logNameAndAppearance
import com.example.valvecontrol.ui.main.viewmodel.IMainViewModel.Event
import com.example.valvecontrol.ui.main.viewmodel.IMainViewModel.PresenterEvent
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

class MainViewModel(
    private val userProvider: IUserProvider
) : BaseDualViewModel<Event, PresenterEvent>(), IMainViewModel {

    override val firebaseUserToken = MutableStateFlow<String?>(null)

    override val settings = MutableStateFlow(emptyList<ValveSetting>())

    override val devices = MutableStateFlow(emptyList<BluetoothDevice>())

    init {
        sendEvent(Event.SubscribeUser)
    }

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.SetUser -> longRunning { handleSetUser(event.user) }
            is Event.SubscribeUser -> longRunning(::handleSubscribeUser)
            is Event.SignUp -> handleSignUp(event)
            is Event.Login -> handleLogin(event)
            is Event.StartScan -> sendPresenterEvent(PresenterEvent.StartScan)
            is Event.GetValveSettings -> sendPresenterEvent(PresenterEvent.GetValveSettings)
            is Event.UpdateValveSettings -> handleUpdateValveSettings(event)
            is Event.SetBluetoothDevices -> longRunning { handleSetBluetoothDevices(event) }
            is Event.ConnectDevice -> sendPresenterEvent(PresenterEvent.ConnectDevice(event.device))
            is Event.AddValveSetting ->
                sendPresenterEvent(PresenterEvent.AddValveSetting(event.setting))
        }
    }

    private suspend fun handleSetUser(user: FirebaseUser) {
        userProvider.setUserFirebaseToken(user.uid)
    }

    private suspend fun handleSubscribeUser() {
        userProvider.getUserFirebaseToken().collect {
            firebaseUserToken.value = it
        }
    }

    private fun handleUpdateValveSettings(event: Event.UpdateValveSettings) {
        settings.value = event.settings.sortedBy(ValveSetting::name)
    }

    private suspend fun handleSetBluetoothDevices(event: Event.SetBluetoothDevices) {
        devices.value = event.devices.sortedByDescending(BluetoothDevice::getAddress)
        event.devices.forEach {
            logNameAndAppearance(it.address)
//            Log.d(MY_TAG, "start device ${it.address}")
//            it.logGattServices()
//            Log.d(MY_TAG, "stop device ${it.address}")
//            delay(300)
        }
    }

    private fun handleSignUp(event: Event.SignUp) {
        sendPresenterEvent(
            PresenterEvent.SignUp(
                email = event.email,
                password = event.password
            )
        )
    }

    private fun handleLogin(event: Event.Login) {
        sendPresenterEvent(
            PresenterEvent.Login(
                email = event.email,
                password = event.password
            )
        )
    }
}