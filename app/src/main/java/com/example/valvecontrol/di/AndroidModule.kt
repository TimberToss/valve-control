package com.example.valvecontrol.di

import com.example.valvecontrol.ui.auth.login.viewmodel.LoginViewModel
import com.example.valvecontrol.ui.auth.signup.viewmodel.SignUpViewModel
import com.example.valvecontrol.ui.auth.welcome.viewmodel.WelcomeViewModel
import com.example.valvecontrol.ui.bluetoothconnection.viewmodel.BluetoothViewModel
import com.example.valvecontrol.ui.profile.editaccount.viewmodel.EditAccountViewModel
import com.example.valvecontrol.ui.profile.support.viewmodel.SupportViewModel
import com.example.valvecontrol.ui.profile.main.viewmodel.ProfileViewModel
import com.example.valvecontrol.ui.profile.theme.viewmodel.ThemeViewModel
import com.example.valvecontrol.ui.setlisting.editvalve.viewmodel.EditValveViewModel
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.ListingViewModel
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
    viewModel { EditAccountViewModel() }
}