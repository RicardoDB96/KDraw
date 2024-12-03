package com.ribod.kdraw.di

import com.ribod.kdraw.data.createDataStore
import com.ribod.kdraw.data.database.getDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { getDatabase(get()) }
    single { createDataStore(context = get()) }
}