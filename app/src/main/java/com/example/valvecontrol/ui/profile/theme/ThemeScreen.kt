package com.example.valvecontrol.ui.profile.theme

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
fun ThemeScreen(navController: NavHostController) {
    navController.previousBackStackEntry?.destination?.route?.let {
        Log.d(MY_TAG," ThemeScreen previousBackStackEntry $it}")
    }
    navController.currentDestination?.hierarchy?.forEachIndexed { index, destination ->
        Log.d(MY_TAG," ThemeScreen $index ${destination.route}")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray)
    ) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = { openAccountScreen(navController) }
        ) {
            Text(text = "Theme")
        }
    }
}

private fun openAccountScreen(navController: NavHostController) {
    navController.navigate(ProfileItem.Account.screenRoute)
}