package com.example.valvecontrol.navigation.bluetooth

sealed class BluetoothItem(
    val screenRoute: String
) {
    object BluetoothScan : BluetoothItem(
        "BluetoothScanRoute"
    )
}
