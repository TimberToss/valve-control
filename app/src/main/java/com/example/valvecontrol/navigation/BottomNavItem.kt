package com.example.valvecontrol.navigation

import com.example.valvecontrol.R


sealed class BottomNavItem(
    val title: String,
    val icon: Int,
    val screenRoute: String
) {

    companion object {
        const val ProfileRoute = "ProfileRoute"
    }

    object Listing : BottomNavItem(
        "Listing",
        R.drawable.ic_baseline_apps_24,
        "ListingRoute"
    )

    object Bluetooth : BottomNavItem(
        "Bluetooth",
        R.drawable.ic_baseline_bluetooth_24,
        "BluetoothRoute"
    )

    object Profile : BottomNavItem(
        "Profile",
        R.drawable.ic_baseline_person_24,
        ProfileRoute
    )

    object Theme : BottomNavItem(
        "Theme",
        R.drawable.ic_baseline_apps_24,
        "ThemeRoute"
    )

    object Account : BottomNavItem(
        "Account",
        R.drawable.ic_baseline_apps_24,
        "AccountRoute"
    )

}