package com.example.valvecontrol.ui.auth.welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.valvecontrol.ui.auth.welcome.viewmodel.IWelcomeViewModel.Event
import com.example.valvecontrol.ui.auth.welcome.viewmodel.WelcomeViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun WelcomeScreen(
    navController: NavHostController,
    viewModel: WelcomeViewModel = getViewModel()
) {
    Column {
        Button(onClick = { viewModel.sendEvent(Event.SignUp) }) {
            Text(text = "Sign Up")
        }
    }
}