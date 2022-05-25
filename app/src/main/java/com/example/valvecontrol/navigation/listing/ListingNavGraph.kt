package com.example.valvecontrol.navigation.listing

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.valvecontrol.navigation.MAIN_ROUTE
import com.example.valvecontrol.ui.setlisting.editvalve.EditValveSettingScreen
import com.example.valvecontrol.ui.setlisting.listing.ListingScreen

fun NavGraphBuilder.listingNavGraph(navController: NavHostController) {
    navigation(
        startDestination = ListingItem.Listing.screenRoute,
        route = MAIN_ROUTE
    ) {
        composable(ListingItem.Listing.screenRoute) {
            ListingScreen(navController)
        }
        composable(ListingItem.EditValveSetting.screenRoute) {
            EditValveSettingScreen(navController)
        }
    }
}