package com.example.valvecontrol.navigation.profile

sealed class ProfileItem(
    val screenRoute: String
) {

    object Theme : ProfileItem(
        "ThemeRoute"
    )

    object Profile : ProfileItem(
        "ProfileRoute"
    )

    object Support : ProfileItem(
        "SupportRoute"
    )

}