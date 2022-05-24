package com.example.valvecontrol.navigation.auth

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
