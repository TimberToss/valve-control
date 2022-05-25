package com.example.valvecontrol.ui.setlisting.listing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.ListingViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun ListingScreen(
    navController: NavHostController,
    viewModel: ListingViewModel = getViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = {}
        ) {
            Text(text = "Listing")
        }
    }
}