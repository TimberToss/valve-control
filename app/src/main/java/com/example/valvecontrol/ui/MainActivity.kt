package com.example.valvecontrol.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.valvecontrol.ValveApp

class MainActivity : ComponentActivity() {

    private val permissionsOldApi = listOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN
    )

    @RequiresApi(Build.VERSION_CODES.S)
    private val permissionsApi31 = listOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT
    )

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { map ->
            map.entries.forEach {
                if (it.value) {
                    Log.d(MY_TAG, "${it.key} permission is granted")
                    Toast.makeText(
                        this,
                        "isGranted",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d(MY_TAG, "${it.key} permission is NOT granted")
                    Toast.makeText(
                        this,
                        "not granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissions = if (isApi31()) {
            permissionsApi31
        } else {
            permissionsOldApi
        }
        checkPermissions(permissions)
        setContent {
            ValveApp()
        }
    }

    private fun checkPermission(permissionName: String) = ContextCompat.checkSelfPermission(
        this,
        permissionName
    ) == PackageManager.PERMISSION_GRANTED.also {
        Log.d(MY_TAG, "$permissionName PERMISSION_GRANTED")
    }

    private fun checkPermissions(permissions: List<String>) {
        permissions.filter {
            checkPermission(it).not()
        }.also {
            Log.d(MY_TAG, "permissions ask")
            requestPermissionLauncher.launch(it.toTypedArray())
        }
//        when {
//
////            shouldShowRequestPermissionRationale(permissionName) -> {
////                // In an educational UI, explain to the user why your app requires this
////                // permission for a specific feature to behave as expected. In this UI,
////                // include a "cancel" or "no thanks" button that allows the user to
////                // continue using your app without granting the permission.
////                Log.d(MY_TAG, "$permissionName shouldShowRequestPermissionRationale")
////            }
//            else -> {
//                // You can directly ask for the permission.
//                // The registered ActivityResultCallback gets the result of this request.
//                Log.d(MY_TAG, "permissions ask")
//                requestPermissionLauncher.launch(permissions.toTypedArray())
//            }
//        }
    }
}