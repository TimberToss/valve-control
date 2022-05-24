package com.example.valvecontrol.navigation

import com.example.valvecontrol.R


sealed class BottomNavItem(
    val title: String,
    val icon: Int,
    val screenRoute: String
) {

    object Listing : BottomNavItem(
        "Listing",
        R.drawable.ic_baseline_apps_24,
        MAIN_ROUTE
    )

    object Bluetooth : BottomNavItem(
        "Bluetooth",
        R.drawable.ic_baseline_bluetooth_24,
        BLUETOOTH_ROUTE
    )

    object Profile : BottomNavItem(
        "Profile",
        R.drawable.ic_baseline_person_24,
        PROFILE_ROUTE
    )
}