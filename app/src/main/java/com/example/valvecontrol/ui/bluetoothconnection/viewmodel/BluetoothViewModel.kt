package com.example.valvecontrol.ui.bluetoothconnection.viewmodel

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.os.Handler
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.example.valvecontrol.base.viewmodel.BaseDualViewModel
import com.example.valvecontrol.ui.MY_TAG
import com.example.valvecontrol.ui.bluetoothconnection.viewmodel.IBluetoothViewModel.Event
import com.example.valvecontrol.ui.bluetoothconnection.viewmodel.IBluetoothViewModel.PresenterEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import splitties.systemservices.bluetoothManager

class BluetoothViewModel : BaseDualViewModel<Event, PresenterEvent>(), IBluetoothViewModel {

    private val SCAN_PERIOD: Long = 10_000

    private var isScanning = false
    private val handler = Handler()
    private val bluetoothLeScanner
        get() = bluetoothManager.adapter.bluetoothLeScanner

    override val bluetoothDevices = MutableStateFlow(listOf("12", "124"))

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.StartScan -> handleStartScan()
            is Event.ConnectDevice -> handleConnectDevice()
        }
    }

    private fun handleStartScan() {
        scanLeDevice()
    }

    private fun handleConnectDevice() {

    }

    @SuppressLint("MissingPermission")
    private fun scanLeDevice() {
        if (!isScanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                isScanning = false
                Log.d(MY_TAG, "stop scan")
                bluetoothLeScanner.stopScan(leScanCallback)
            }, SCAN_PERIOD)
            isScanning = true
            Log.d(MY_TAG, "start scan")
            bluetoothLeScanner.startScan(leScanCallback)
        } else {
            isScanning = false
            bluetoothLeScanner.stopScan(leScanCallback)
        }
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.d(MY_TAG, "scan result $callbackType address: ${result.device}")
            super.onScanResult(callbackType, result)
        }

        override fun onScanFailed(errorCode: Int) {
            Log.d(MY_TAG, "scan fail errorCode $errorCode")
        }
    }

}