package com.ribod.kdraw

import android.app.Application
import com.ribod.kdraw.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class KDrawApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@KDrawApp)
        }
    }
}