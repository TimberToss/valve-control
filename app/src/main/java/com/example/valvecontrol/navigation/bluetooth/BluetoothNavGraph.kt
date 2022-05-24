package com.example.valvecontrol.navigation.bluetooth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.valvecontrol.navigation.BLUETOOTH_ROUTE
import com.example.valvecontrol.ui.bluetoothconnection.BluetoothConnectionScreen

fun NavGraphBuilder.bluetoothNavGraph(navController: NavHostController) {
    navigation(
        startDestination = BluetoothItem.BluetoothScan.screenRoute,
        route = BLUETOOTH_ROUTE
    ) {
        composable(BluetoothItem.BluetoothScan.screenRoute) {
            BluetoothConnectionScreen(navController)
        }
    }
}