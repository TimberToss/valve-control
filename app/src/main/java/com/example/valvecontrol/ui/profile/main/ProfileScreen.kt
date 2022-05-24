package com.example.valvecontrol.ui.profile.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.example.valvecontrol.navigation.ProfileItem
import com.example.valvecontrol.ui.MY_TAG

@Composable
fun ProfileScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = { openThemeScreen(navController) }
        ) {
            Text(text = "Profile")
        }
    }
}

private fun openThemeScreen(navController: NavHostController) {
    navController.currentDestination?.hierarchy?.forEach {
        Log.d(MY_TAG,"${it.route}")
    }
    navController.navigate(ProfileItem.Theme.screenRoute)
}