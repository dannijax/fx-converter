package com.danijax.currencyconverter

import android.app.Application
import androidx.work.Configuration
// Setup global configuration for Work manager
class App : Application(), Configuration.Provider {

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}