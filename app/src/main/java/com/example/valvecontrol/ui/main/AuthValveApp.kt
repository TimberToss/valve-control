package com.example.valvecontrol.ui.main

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.valvecontrol.navigation.auth.AuthNavGraph
import com.example.valvecontrol.theme.ValveTheme

@Composable
fun AuthValveApp() {
    val navController = rememberNavController()
    ValveTheme {
        Scaffold {
            AuthNavGraph(navController = navController)
        }
    }
}