package com.example.valvecontrol.navigation

sealed class ListingItem(
    val  screenRoute: String
) {
    object Listing : ListingItem(
        "ListingRoute"
    )
}
