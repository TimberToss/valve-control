package com.example.valvecontrol.ui.profile.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.valvecontrol.navigation.profile.ProfileItem
import com.example.valvecontrol.ui.profile.theme.viewmodel.ThemeViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun ThemeScreen(
    navController: NavHostController,
    viewModel: ThemeViewModel = getViewModel()
) {
    val number = viewModel.number.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray)
    ) {
        Column {
            Button(
                modifier = Modifier.padding(top = 200.dp),
                onClick = { openAccountScreen(navController) }
            ) {
                Text(text = "Theme")
            }
            Button(
                modifier = Modifier.padding(top = 10.dp),
                onClick = { viewModel.updateNumber(5) }
            ) {
                Text(text = "Update view model state")
            }
            Text(
                text = "${number.value}",
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

private fun openAccountScreen(navController: NavHostController) {
    navController.navigate(ProfileItem.Support.screenRoute)
}