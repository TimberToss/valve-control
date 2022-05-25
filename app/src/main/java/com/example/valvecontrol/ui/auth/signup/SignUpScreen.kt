package com.example.valvecontrol.ui.auth.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.valvecontrol.R
import com.example.valvecontrol.ui.MY_TAG
import com.example.valvecontrol.ui.auth.signup.viewmodel.ISignUpViewModel
import com.example.valvecontrol.ui.auth.signup.viewmodel.ISignUpViewModel.Event
import com.example.valvecontrol.ui.auth.signup.viewmodel.ISignUpViewModel.PresenterEvent
import com.example.valvecontrol.ui.auth.signup.viewmodel.SignUpViewModel
import com.example.valvecontrol.ui.main.viewmodel.IMainViewModel
import com.example.valvecontrol.ui.main.viewmodel.MainViewModel
import org.koin.androidx.compose.getViewModel


@Composable
fun SignUpScreen(
    navController: NavHostController,
    viewModel: ISignUpViewModel = getViewModel<SignUpViewModel>(),
    mainViewModel: IMainViewModel = getViewModel<MainViewModel>()
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.presenterEvent.collect {
            handlePresenterEvent(it, navController)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = email,
            onValueChange = { viewModel.sendEvent(Event.EmailUpdate(it)) },
            modifier = Modifier.padding(16.dp)
        )
        TextField(
            value = password,
            onValueChange = { viewModel.sendEvent(Event.PasswordUpdate(it)) },
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = {
                Log.d(MY_TAG, "Sign up clicked")
                mainViewModel.sendEvent(
                    IMainViewModel.Event.SignUp(
                        email = email,
                        password = password
                    )
                )
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.sign_up_screen_sign_up)
            )
        }
    }
}

private fun handlePresenterEvent(
    event: PresenterEvent,
    navController: NavHostController
) =
    when (event) {
        else -> {}
    }