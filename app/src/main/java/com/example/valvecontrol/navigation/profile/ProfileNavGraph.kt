package com.example.valvecontrol.navigation.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.valvecontrol.navigation.PROFILE_ROUTE
import com.example.valvecontrol.ui.profile.support.SupportScreen
import com.example.valvecontrol.ui.profile.main.ProfileScreen
import com.example.valvecontrol.ui.profile.theme.ThemeScreen

fun NavGraphBuilder.profileNavGraph(navController: NavHostController) {
    navigation(
        startDestination = ProfileItem.Profile.screenRoute,
        route = PROFILE_ROUTE
    ) {
        composable(ProfileItem.Profile.screenRoute) {
            ProfileScreen(navController)
        }
        composable(ProfileItem.Theme.screenRoute) {
            ThemeScreen(navController)
        }
        composable(ProfileItem.Support.screenRoute) {
            SupportScreen(navController)
        }
    }
}