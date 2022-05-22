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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.valvecontrol.R
import com.example.valvecontrol.ui.bluetoothconnection.BluetoothConnectionScreen
import com.example.valvecontrol.ui.profile.account.AccountScreen
import com.example.valvecontrol.ui.profile.main.ProfileScreen
import com.example.valvecontrol.ui.profile.theme.ThemeScreen
import com.example.valvecontrol.ui.setlisting.ListingScreen

const val MAIN_ROUTE = "MAIN_ROUTE"

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Listing,
        BottomNavItem.Bluetooth,
        BottomNavItem.Profile,
    )
    androidx.compose.material.BottomNavigation(
        backgroundColor = colorResource(id = R.color.teal_200),
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 9.sp) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = true,
                selected = resolveSelected(navController.previousBackStackEntry, item.screenRoute),
//                selected = currentDestination?.hierarchy?.any {
//                    it.route == item.screenRoute
//                } == true,
                onClick = {
//                    TabContent(navController, item)
                    navController.navigate(item.screenRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

//                        navController.graph.startDestinationRoute?.let { screenRoute ->
//                            popUpTo(screenRoute) {
//                                saveState = true
//                            }
//                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun TabContent(navController: NavController, screen: BottomNavItem) {
    when (screen) {
        BottomNavItem.Listing -> ListingScreen(navController)
        BottomNavItem.Bluetooth -> {
        }
        BottomNavItem.Profile -> ProfileTab()
        else -> {
        }
    }
}

@Composable
fun ProfileTab() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Profile.screenRoute
    ) {
        composable(BottomNavItem.Profile.screenRoute) {
            ProfileScreen(navController)
        }
        composable(BottomNavItem.Theme.screenRoute) {
            ThemeScreen(navController)
        }
        composable(BottomNavItem.Account.screenRoute) {
            AccountScreen(navController)
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Listing.screenRoute, route = MAIN_ROUTE) {
        composable(BottomNavItem.Listing.screenRoute) {
            ListingScreen(navController)
        }
        composable(BottomNavItem.Bluetooth.screenRoute) {
            BluetoothConnectionScreen(navController)
        }
        composable(BottomNavItem.Profile.screenRoute) {
            ProfileScreen(navController)
        }
        composable(BottomNavItem.Theme.screenRoute) {
            ThemeScreen(navController)
        }
        composable(BottomNavItem.Account.screenRoute) {
            AccountScreen(navController)
        }
    }
}

fun resolveSelected(previousBackStackEntry: NavBackStackEntry?, screenRoute: String) =
    screenRoute == previousBackStackEntry?.destination?.route

//fun resolveSelected(previousBackStackEntry: NavBackStackEntry, screenRoute: String): Boolean {
//    return if (MAIN_ROUTE != previousBackStackEntry.destination.route) {
//        resolveSelected(previousBackStackEntry.destination.parent., screenRoute)
//    } else {
//        screenRoute == previousBackStackEntry.destination.route
//    }
//}