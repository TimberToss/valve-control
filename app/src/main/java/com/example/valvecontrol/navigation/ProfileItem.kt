package com.example.valvecontrol.navigation

sealed class ProfileItem(
    val screenRoute: String
) {

    object Theme : ProfileItem(
        "ThemeRoute"
    )

    object Profile : ProfileItem(
        "ProfileRoute"
    )

    object Account : ProfileItem(
        "AccountRoute"
    )

}