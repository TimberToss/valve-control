package com.example.valvecontrol.ui.bluetoothconnection

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.valvecontrol.R
import com.example.valvecontrol.theme.ValveTypography
import com.example.valvecontrol.ui.MY_TAG
import com.example.valvecontrol.ui.bluetoothconnection.viewmodel.BluetoothViewModel
import com.example.valvecontrol.ui.bluetoothconnection.viewmodel.IBluetoothViewModel
import com.example.valvecontrol.ui.bluetoothconnection.viewmodel.IBluetoothViewModel.Event
import com.example.valvecontrol.ui.main.viewmodel.IMainViewModel
import com.example.valvecontrol.ui.main.viewmodel.MainViewModel
import com.example.valvecontrol.ui.resolveNeededPermissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BluetoothConnectionScreen(
    navController: NavHostController,
    viewModel: IBluetoothViewModel = getViewModel<BluetoothViewModel>(),
    mainViewModel: IMainViewModel = getViewModel<MainViewModel>()
) {
    val bluetoothDevices by mainViewModel.devices.collectAsState()

    val multiplePermissionsState = rememberMultiplePermissionsState(resolveNeededPermissions())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(rememberScrollState(), Orientation.Vertical)
                .padding(bottom = 140.dp, top = 16.dp)
        ) {
            bluetoothDevices.forEach { device ->
                BluetoothDeviceItem(device) {
                    viewModel.sendEvent(Event.ConnectDevice(it))
                }
            }
        }
        Button(
            onClick = {
                onConnectClick(
                    isAllPermissionsGranted = multiplePermissionsState.allPermissionsGranted,
                    positiveAction = {
                        mainViewModel.sendEvent(IMainViewModel.Event.StartScan)
//                        viewModel.sendEvent(Event.StartScan)
                    },
                    negativeAction = { multiplePermissionsState.launchMultiplePermissionRequest() }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 80.dp),
        ) {
            Text(text = stringResource(id = R.string.bluetooth_screen_scan))
        }
    }
}

@Composable
private fun BluetoothDeviceItem(
    device: BluetoothDevice,
    onClick: (BluetoothDevice) -> Unit
) = Card(
    shape = RoundedCornerShape(30),
    elevation = 2.dp,
    modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(start = 16.dp, end = 16.dp, bottom = 22.dp)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.bluetooth_screen_mac_address),
            style = ValveTypography.h1,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 12.dp, end = 12.dp)
        )
        Text(
            text = device.address,
            style = ValveTypography.body1,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 12.dp, end = 12.dp, top = 10.dp)
        )
        Button(
            onClick = { onClick(device) },
            modifier = Modifier
                .align(Alignment.End)
                .padding(start = 12.dp, end = 12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.bluetooth_screen_connect),
                style = ValveTypography.body1,
            )
        }
//        Row(
//            modifier = Modifier.weight(1.0f)
//        ) {
//            Text(
//                text = setting.name,
//                style = ValveTypography.h2,
//                modifier = Modifier
//                    .weight(1.0f)
//                    .padding(start = 12.dp, end = 12.dp, bottom = 2.dp)
//            )
//        }
//        Row(
//            verticalAlignment = Alignment.Bottom,
//            modifier = Modifier.padding(bottom = 12.dp)
//        ) {
//            Text(
//                text = setting.segment1.toString(),
//                style = ValveTypography.h2,
//                modifier = Modifier
//                    .weight(1.0f)
//                    .padding(start = 12.dp, end = 12.dp, bottom = 2.dp)
//            )
//            Text(
//                text = setting.segment2.toString(),
//                style = ValveTypography.h2,
//                modifier = Modifier
//                    .weight(1.0f)
//                    .padding(start = 12.dp, end = 12.dp, bottom = 2.dp)
//            )
//            Text(
//                text = setting.segment3.toString(),
//                style = ValveTypography.h2,
//                modifier = Modifier
//                    .weight(1.0f)
//                    .padding(start = 12.dp, end = 12.dp, bottom = 2.dp)
//            )
//            Text(
//                text = setting.segment4.toString(),
//                style = ValveTypography.h2,
//                modifier = Modifier
//                    .weight(1.0f)
//                    .padding(start = 12.dp, end = 12.dp, bottom = 2.dp)
//            )
//        }
    }
}

private fun onConnectClick(
    isAllPermissionsGranted: Boolean,
    positiveAction: () -> Unit,
    negativeAction: () -> Unit,
) {
    Log.d(MY_TAG, "$isAllPermissionsGranted")
    if (isAllPermissionsGranted) {
        positiveAction()
    } else {
        negativeAction()
    }
}