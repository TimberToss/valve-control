package com.example.valvecontrol.navigation

import com.example.valvecontrol.R

sealed class AuthItem(
    val screenRoute: String
) {
    object Welcome : AuthItem(
        "WelcomeRoute"
    )

    object Login : AuthItem(
        "LoginRoute"
    )

    object SignUp : AuthItem(
        "SignUpRoute"
    )
}
