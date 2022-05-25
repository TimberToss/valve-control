package com.example.valvecontrol.ui.profile.support

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.valvecontrol.ui.profile.support.viewmodel.SupportViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SupportScreen(
    navController: NavHostController,
    viewModel: SupportViewModel = getViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Green)
    ) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = navController::popBackStack
        ) {
            Text(text = "Support")
        }
    }
}