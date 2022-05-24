package com.example.valvecontrol.ui.profile.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ThemeViewModel : ViewModel() {

    val number = MutableStateFlow(1)

    fun updateNumber(n: Int) {
        number.value = n
    }
}