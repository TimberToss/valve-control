package com.example.valvecontrol.navigation

import android.util.Log
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.navigation.navigation
import com.example.valvecontrol.R
import com.example.valvecontrol.ui.MY_TAG
import com.example.valvecontrol.ui.bluetoothconnection.BluetoothConnectionScreen
import com.example.valvecontrol.ui.profile.account.AccountScreen
import com.example.valvecontrol.ui.profile.main.ProfileScreen
import com.example.valvecontrol.ui.profile.theme.ThemeScreen
import com.example.valvecontrol.ui.setlisting.ListingScreen

const val ROOT_ROUTE = "ROOT_ROUTE"
const val MAIN_ROUTE = "MAIN_ROUTE"
const val PROFILE_ROUTE = "PROFILE_ROUTE"
const val BLUETOOTH_ROUTE = "BLUETOOTH_ROUTE"
const val AUTH_ROUTE = "AUTH_ROUTE"

val bottomItems = listOf(
    BottomNavItem.Listing,
    BottomNavItem.Bluetooth,
    BottomNavItem.Profile,
)

@Composable
fun BottomNavigation(navController: NavHostController) {

    androidx.compose.material.BottomNavigation(
        backgroundColor = colorResource(id = R.color.teal_200),
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
                alwaysShowLabel = true,
//                selected = resolveSelected(
//                    currentDestination,
//                    navController.previousBackStackEntry,
//                    item.screenRoute
//                ),
                selected = currentDestination?.hierarchy?.any {
                    it.route == item.screenRoute
                } == true,
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
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController,
        startDestination = MAIN_ROUTE,
        route = ROOT_ROUTE
    ) {
        navigation(
            startDestination = ListingItem.Listing.screenRoute,
            route = MAIN_ROUTE
        ) {
            composable(ListingItem.Listing.screenRoute) {
                ListingScreen(navController)
            }
//            composable(BottomNavItem.Bluetooth.screenRoute) {
//                BluetoothConnectionScreen(navController)
//            }
//            composable(BottomNavItem.Profile.screenRoute) {
//                ProfileScreen(navController)
//            }
        }
        navigation(
            startDestination = AuthItem.Welcome.screenRoute,
            route = AUTH_ROUTE
        ) {
            composable(AuthItem.Welcome.screenRoute) {
                ThemeScreen(navController)
            }
            composable(AuthItem.Login.screenRoute) {
                AccountScreen(navController)
            }
            composable(AuthItem.SignUp.screenRoute) {
                AccountScreen(navController)
            }
        }
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
            composable(ProfileItem.Account.screenRoute) {
                AccountScreen(navController)
            }
        }
        navigation(
            startDestination = BluetoothItem.BluetoothScan.screenRoute,
            route = BLUETOOTH_ROUTE
        ) {
            composable(BluetoothItem.BluetoothScan.screenRoute) {
                BluetoothConnectionScreen(navController)
            }
        }
    }
}

fun resolveSelected(
    currentDestination: NavDestination?,
    previousBackStackEntry: NavBackStackEntry?,
    screenRoute: String
) = when {
    screenRoute == currentDestination?.route -> true
    screenRoute == previousBackStackEntry?.destination?.route
            && bottomItems.map { it.screenRoute }.contains(currentDestination?.route).not() -> true
    else -> false
}.also { Log.d(MY_TAG, "$screenRoute resolveSelected") }

//fun resolveSelected(previousBackStackEntry: NavBackStackEntry, screenRoute: String): Boolean {
//    return if (MAIN_ROUTE != previousBackStackEntry.destination.route) {
//        resolveSelected(previousBackStackEntry.destination.parent., screenRoute)
//    } else {
//        screenRoute == previousBackStackEntry.destination.route
//    }
//}