package com.pertamina.absensiapplication.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class PermitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PermitApplication)
            modules(appComponent)
        }
    }
}