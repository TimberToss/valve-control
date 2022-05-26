package com.example.valvecontrol.navigation

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.valvecontrol.R
import com.example.valvecontrol.navigation.auth.authNavGraph
import com.example.valvecontrol.navigation.bluetooth.bluetoothNavGraph
import com.example.valvecontrol.navigation.listing.listingNavGraph
import com.example.valvecontrol.navigation.profile.profileNavGraph

const val ROOT_ROUTE = "ROOT_ROUTE"
const val MAIN_ROUTE = "MAIN_ROUTE"
const val PROFILE_ROUTE = "PROFILE_ROUTE"
const val BLUETOOTH_ROUTE = "BLUETOOTH_ROUTE"
const val AUTH_ROUTE = "AUTH_ROUTE"
const val START_ROUTE = "START_ROUTE"

val bottomItems = listOf(
    BottomNavItem.Listing,
    BottomNavItem.Bluetooth,
    BottomNavItem.Profile,
)

@Composable
fun BottomNavigation(navController: NavHostController) {

    androidx.compose.material.BottomNavigation(
//        backgroundColor = colorResource(id = R.color.teal_200),
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 9.sp) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = false,
                selected = currentDestination?.hierarchy?.any {
                    it.route == item.screenRoute
                } == true,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController,
        startDestination = MAIN_ROUTE,
        route = ROOT_ROUTE
    ) {
//        authNavGraph(navController)
        listingNavGraph(navController)
        bluetoothNavGraph(navController)
        profileNavGraph(navController)
    }
}