package com.example.valvecontrol.ui.profile.theme.viewmodel

import com.example.valvecontrol.base.BaseDualViewModel
import com.example.valvecontrol.ui.profile.theme.viewmodel.IThemeViewModel.Event
import com.example.valvecontrol.ui.profile.theme.viewmodel.IThemeViewModel.PresenterEvent
import kotlinx.coroutines.flow.MutableStateFlow

class ThemeViewModel : BaseDualViewModel<Event, PresenterEvent>(), IThemeViewModel {

    val number = MutableStateFlow(1)

    fun updateNumber(n: Int) {
        number.value = n
    }

    override suspend fun handleEvent(event: Event) {
        TODO("Not yet implemented")
    }
}