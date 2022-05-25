package com.example.valvecontrol.ui.setlisting.listing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.valvecontrol.data.model.ValveSetting
import com.example.valvecontrol.navigation.listing.ListingItem
import com.example.valvecontrol.theme.ValveTypography
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.IListingViewModel
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.IListingViewModel.Event
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.IListingViewModel.PresenterEvent
import com.example.valvecontrol.ui.setlisting.listing.viewmodel.ListingViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun ListingScreen(
    navController: NavHostController,
    viewModel: IListingViewModel = getViewModel<ListingViewModel>()
) {
    val settings by viewModel.settings.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.presenterEvent.collect {
            handlePresenterEvent(it, navController)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(vertical = 16.dp)
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
    ) {
        settings.forEach { setting ->
            SettingItem(
                setting = setting,
                onClick = { viewModel.sendEvent(Event.ApplyValveSetting(it)) }
            )
        }
    }

}

@Composable
private fun SettingItem(
    setting: ValveSetting,
    onClick: (ValveSetting) -> Unit
) = Card(
    shape = RoundedCornerShape(30),
    elevation = 2.dp,
    modifier = Modifier
        .fillMaxWidth()
        .height(160.dp)
        .padding(start = 16.dp, end = 16.dp, bottom = 22.dp)
        .clickable { onClick(setting) }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.weight(1.0f)
        ) {
            Text(
                text = setting.name,
                style = ValveTypography.h2,
                modifier = Modifier
                    .weight(1.0f)
                    .padding(start = 12.dp, end = 12.dp, bottom = 2.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = setting.segment1.toString(),
                style = ValveTypography.h2,
                modifier = Modifier
                    .weight(1.0f)
                    .padding(start = 12.dp, end = 12.dp, bottom = 2.dp)
            )
            Text(
                text = setting.segment2.toString(),
                style = ValveTypography.h2,
                modifier = Modifier
                    .weight(1.0f)
                    .padding(start = 12.dp, end = 12.dp, bottom = 2.dp)
            )
            Text(
                text = setting.segment3.toString(),
                style = ValveTypography.h2,
                modifier = Modifier
                    .weight(1.0f)
                    .padding(start = 12.dp, end = 12.dp, bottom = 2.dp)
            )
            Text(
                text = setting.segment4.toString(),
                style = ValveTypography.h2,
                modifier = Modifier
                    .weight(1.0f)
                    .padding(start = 12.dp, end = 12.dp, bottom = 2.dp)
            )
        }
    }
}

private fun handlePresenterEvent(event: PresenterEvent, navController: NavHostController) =
    when (event) {
        is PresenterEvent.OpenEditValveSettingScreen ->
            openEditAccountSettingScreen(navController, event)
    }

private fun openEditAccountSettingScreen(
    navController: NavHostController,
    event: PresenterEvent.OpenEditValveSettingScreen
) {
    navController.navigate(ListingItem.EditValveSetting.screenRoute)
}