package com.example.valvecontrol.navigation.listing

sealed class ListingItem(
    val screenRoute: String
) {
    object Listing : ListingItem(
        "ListingRoute"
    )

    object EditValveSetting : ListingItem(
        "EditValveSettingRoute"
    )
}
