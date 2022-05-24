package com.example.valvecontrol.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.valvecontrol.navigation.AUTH_ROUTE
import com.example.valvecontrol.ui.auth.login.LoginScreen
import com.example.valvecontrol.ui.auth.signup.SignUpScreen
import com.example.valvecontrol.ui.auth.welcome.WelcomeScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        startDestination = AuthItem.Welcome.screenRoute,
        route = AUTH_ROUTE
    ) {
        composable(AuthItem.Welcome.screenRoute) {
            WelcomeScreen(navController)
        }
        composable(AuthItem.Login.screenRoute) {
            LoginScreen(navController)
        }
        composable(AuthItem.SignUp.screenRoute) {
            SignUpScreen(navController)
        }
    }
}