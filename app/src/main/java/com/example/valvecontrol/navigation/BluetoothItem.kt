package com.example.valvecontrol.navigation

sealed class BluetoothItem(
    val screenRoute: String
) {
    object BluetoothScan : BluetoothItem(
        "BluetoothScanRoute"
    )
}
