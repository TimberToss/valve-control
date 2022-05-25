package com.example.valvecontrol.ui.main

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.example.valvecontrol.theme.ValveTheme
import androidx.navigation.compose.rememberNavController
import com.example.valvecontrol.navigation.*
import com.example.valvecontrol.navigation.auth.authNavGraph

@Composable
fun ValveApp() {
    val navController = rememberNavController()
    ValveTheme {
        Scaffold(
            bottomBar = { BottomNavigation(navController = navController) }
        ) {
            NavigationGraph(navController = navController)
        }
//        val appState = rememberJetsnackAppState()
//        JetsnackScaffold(
//            bottomBar = {
//                if (appState.shouldShowBottomBar) {
//                    JetsnackBottomBar(
//                        tabs = appState.bottomBarTabs,
//                        currentRoute = appState.currentRoute!!,
//                        navigateToRoute = appState::navigateToBottomBarRoute
//                    )
//                }
//            },
//            snackbarHost = {
//                SnackbarHost(
//                    hostState = it,
//                    modifier = Modifier.systemBarsPadding(),
//                    snackbar = { snackbarData -> JetsnackSnackbar(snackbarData) }
//                )
//            },
//            scaffoldState = appState.scaffoldState
//        ) { innerPaddingModifier ->
//            NavHost(
//                navController = appState.navController,
//                startDestination = MainDestinations.HOME_ROUTE,
//                modifier = Modifier.padding(innerPaddingModifier)
//            ) {
//                jetsnackNavGraph(
//                    onSnackSelected = appState::navigateToSnackDetail,
//                    upPress = appState::upPress
//                )
//            }
//        }
    }


}

//private fun NavGraphBuilder.jetsnackNavGraph(
//    onSnackSelected: (Long, NavBackStackEntry) -> Unit,
//    upPress: () -> Unit
//) {
//    navigation(
//        route = MainDestinations.HOME_ROUTE,
//        startDestination = HomeSections.FEED.route
//    ) {
//        addHomeGraph(onSnackSelected)
//    }
//    composable(
//        "${MainDestinations.SNACK_DETAIL_ROUTE}/{${MainDestinations.SNACK_ID_KEY}}",
//        arguments = listOf(navArgument(MainDestinations.SNACK_ID_KEY) { type = NavType.LongType })
//    ) { backStackEntry ->
//        val arguments = requireNotNull(backStackEntry.arguments)
//        val snackId = arguments.getLong(MainDestinations.SNACK_ID_KEY)
//        SnackDetail(snackId, upPress)
//    }
//}