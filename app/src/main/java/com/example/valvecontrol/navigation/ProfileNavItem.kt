package com.example.valvecontrol.navigation

import com.example.valvecontrol.R

sealed class ProfileNavItem(
    val title: String,
    val icon: Int,
    val screenRoute: String
) {

    object Theme : ProfileNavItem(
        "Theme",
        R.drawable.ic_baseline_apps_24,
        "ThemeRoute"
    )

    object Bluetooth : ProfileNavItem(
        "Bluetooth",
        R.drawable.ic_baseline_bluetooth_24,
        "BluetoothRoute"
    )

    object Profile : ProfileNavItem(
        "Profile",
        R.drawable.ic_baseline_person_24,
        "ProfileRoute"
    )

}