package com.example.valvecontrol.ui.profile.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.valvecontrol.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.valvecontrol.navigation.profile.ProfileItem
import com.example.valvecontrol.theme.ValveTypography
import com.example.valvecontrol.ui.profile.main.viewmodel.IProfileViewModel
import com.example.valvecontrol.ui.profile.main.viewmodel.IProfileViewModel.Event
import com.example.valvecontrol.ui.profile.main.viewmodel.IProfileViewModel.PresenterEvent
import com.example.valvecontrol.ui.profile.main.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.getViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: IProfileViewModel = getViewModel<ProfileViewModel>(),
) {
    val name by viewModel.name.collectAsState()
    val description by viewModel.description.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.presenterEvent.collect {
            handlePresenterEvent(it, navController)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(0.dp, 150.dp)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            elevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_person_24),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, CircleShape)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = name,
                        style = ValveTypography.body1,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                    )
                    Text(
                        text = description,
                        modifier = Modifier.weight(1.0f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.End)
                            .clickable {
                                viewModel.sendEvent(
                                    Event.EditAccountSettings(
                                        name = name,
                                        description = description
                                    )
                                )
                            }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        ProfileItem(
            R.string.profile_screen_change_theme,
            R.drawable.ic_baseline_wb_sunny_24
        ) {
            viewModel.sendEvent(Event.ChangeThemeClicked)
        }
        ProfileItem(
            R.string.profile_screen_call_support,
            R.drawable.ic_baseline_people_24
        ) {
            viewModel.sendEvent(Event.SupportClicked)
        }
        ProfileItem(
            R.string.profile_screen_log_out,
            R.drawable.ic_baseline_exit_to_app_24
        ) {
            viewModel.sendEvent(Event.LogOutClicked)
        }
    }
}

@Composable
private fun ProfileItem(
    @StringRes textRes: Int,
    @DrawableRes drawableRes: Int,
    onClick: () -> Unit
) = Card(
    shape = RoundedCornerShape(30),
    elevation = 2.dp,
    modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp, bottom = 22.dp)
        .clickable { onClick() }
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = null,
            modifier = Modifier
        )
        Text(
            text = stringResource(textRes),
            style = ValveTypography.h2,
            modifier = Modifier
                .weight(1.0f)
                .padding(start = 12.dp, end = 12.dp, bottom = 2.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24),
            contentDescription = null,
            modifier = Modifier
        )
    }
}

private fun handlePresenterEvent(event: PresenterEvent, navController: NavHostController) =
    when (event) {
        is PresenterEvent.OpenSupportScreen -> openSupportScreen(navController)
        is PresenterEvent.OpenThemeScreen -> openThemeScreen(navController)
        is PresenterEvent.OpenEditAccountSettingScreen ->
            openEditAccountSettingScreen(navController, event)
    }

private fun openThemeScreen(navController: NavHostController) {
    navController.navigate(ProfileItem.Theme.screenRoute)
}

private fun openSupportScreen(navController: NavHostController) {
    navController.navigate(ProfileItem.Support.screenRoute)
}

private fun openEditAccountSettingScreen(
    navController: NavHostController,
    event: PresenterEvent.OpenEditAccountSettingScreen
) {
    navController.navigate(ProfileItem.EditAccountSetting.screenRoute)
}