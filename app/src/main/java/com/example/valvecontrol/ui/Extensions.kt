package com.example.valvecontrol.ui

import android.os.Build

const val MY_TAG = "MY_TAG"

fun isApi(apiNumber: Int) = Build.VERSION.SDK_INT >= apiNumber