package com.example.valvecontrol.ui.auth.welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.valvecontrol.navigation.auth.AuthItem
import com.example.valvecontrol.ui.auth.welcome.viewmodel.IWelcomeViewModel
import com.example.valvecontrol.ui.auth.welcome.viewmodel.IWelcomeViewModel.Event
import com.example.valvecontrol.ui.auth.welcome.viewmodel.IWelcomeViewModel.PresenterEvent
import com.example.valvecontrol.ui.auth.welcome.viewmodel.WelcomeViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun WelcomeScreen(
    navController: NavHostController,
    viewModel: IWelcomeViewModel = getViewModel<WelcomeViewModel>(),
) {
    LaunchedEffect(Unit) {
        viewModel.presenterEvent.collect {
            handlePresenterEvent(it, navController)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = { viewModel.sendEvent(Event.SignUpClicked) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 100.dp)
        ) {
            Text(text = "Sign Up")
        }
        Button(
            onClick = { viewModel.sendEvent(Event.LoginClicked) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp)
        ) {
            Text(text = "Log in")
        }
    }
}

private fun handlePresenterEvent(event: PresenterEvent, navController: NavHostController) {
    when (event) {
        is PresenterEvent.OpenLoginScreen -> navController.navigate(AuthItem.Login.screenRoute)
        is PresenterEvent.OpenSignUpScreen -> navController.navigate(AuthItem.SignUp.screenRoute)
    }
}