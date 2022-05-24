package com.example.valvecontrol.di

import com.example.valvecontrol.ui.auth.login.LoginViewModel
import com.example.valvecontrol.ui.auth.signup.SignUpViewModel
import com.example.valvecontrol.ui.auth.welcome.WelcomeViewModel
import com.example.valvecontrol.ui.bluetoothconnection.BluetoothViewModel
import com.example.valvecontrol.ui.profile.account.SupportViewModel
import com.example.valvecontrol.ui.profile.main.ProfileViewModel
import com.example.valvecontrol.ui.profile.theme.ThemeViewModel
import com.example.valvecontrol.ui.setlisting.editvalve.EditValveViewModel
import com.example.valvecontrol.ui.setlisting.listing.ListingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    viewModel { ListingViewModel() }
    viewModel { ProfileViewModel() }
    viewModel { BluetoothViewModel() }

    viewModel { EditValveViewModel() }

    viewModel { ThemeViewModel() }
    viewModel { SupportViewModel() }

    viewModel { LoginViewModel() }
    viewModel { SignUpViewModel() }
    viewModel { WelcomeViewModel() }
}