package com.example.valvecontrol.ui.profile.account

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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.valvecontrol.navigation.BottomNavItem
import com.example.valvecontrol.ui.MY_TAG

@Composable
fun AccountScreen(navController: NavController) {
    navController.graph.findNode(BottomNavItem.Bluetooth.screenRoute)?.let {
        Log.d(MY_TAG," AccountScreen graph.findNode $it}")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Green)
    ) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = navController::popBackStack
        ) {
            Text(text = "Account")
        }
    }
}