package com.example.valvecontrol.ui.profile.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.valvecontrol.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.valvecontrol.navigation.profile.ProfileItem
import com.example.valvecontrol.ui.profile.main.viewmodel.ProfileViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = getViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Card(
            modifier = Modifier.padding(),
            elevation = 4.dp
        ) {
            Row(
                modifier = Modifier,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_person_24),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier,
                ) {
                    Text(
                        text = "Igor Mashtakov",
                        modifier = Modifier
                    )
                    Text(
                        text = "Work as hard as possible",
                        modifier = Modifier
                    )
                    Text(
                        text = "Edit",
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

private fun openThemeScreen(navController: NavHostController) {
    navController.navigate(ProfileItem.Theme.screenRoute)
}