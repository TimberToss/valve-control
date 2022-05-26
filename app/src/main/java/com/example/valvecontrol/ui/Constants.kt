package com.example.valvecontrol.ui

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

object Constants {
    const val GATT_MAX_MTU_SIZE = 517

    const val USERS_TABLE_NAME = "users"
    const val USERS_TABLE_EMAIL_FIELD = "email"
    const val USERS_TABLE_SETTINGS_COLLECTION = "valveSettings"
    const val SETTINGS_TABLE_NAME_FIELD = "name"
    const val SETTINGS_TABLE_SEGMENT1_FIELD = "segment1"
    const val SETTINGS_TABLE_SEGMENT2_FIELD = "segment2"
    const val SETTINGS_TABLE_SEGMENT3_FIELD = "segment3"
    const val SETTINGS_TABLE_SEGMENT4_FIELD = "segment4"

    val SERVICE_UUID: UUID = UUID.fromString("e4205d54-dd14-11ec-9d64-0242ac120002")
    val CHARACTERISTIC_SEGMENT1_UUID: UUID = UUID.fromString("f0dd21f8-dd14-11ec-9d64-0242ac120002")
    val CHARACTERISTIC_SEGMENT2_UUID: UUID = UUID.fromString("adfa76bc-dd26-11ec-9d64-0242ac120002")
    val CHARACTERISTIC_SEGMENT3_UUID: UUID = UUID.fromString("b7a1fe4c-dd26-11ec-9d64-0242ac120002")
    val CHARACTERISTIC_SEGMENT4_UUID: UUID = UUID.fromString("bca2f9f0-dd26-11ec-9d64-0242ac120002")

    val allCharacteristics = listOf(
        CHARACTERISTIC_SEGMENT1_UUID,
        CHARACTERISTIC_SEGMENT2_UUID,
        CHARACTERISTIC_SEGMENT3_UUID,
        CHARACTERISTIC_SEGMENT4_UUID
    )

    val PERMISSIONS_OLD_API = listOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    val PERMISSIONS_API_29 = listOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.S)
    val PERMISSIONS_API_31 = listOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

}