package com.example.valvecontrol

import android.app.Application
import com.example.valvecontrol.di.androidModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class ValveApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ValveApplication)
            modules(androidModule)
        }
    }
}