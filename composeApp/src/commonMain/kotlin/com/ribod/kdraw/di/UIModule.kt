package com.ribod.kdraw.di

import com.ribod.kdraw.ui.main.tabs.home.HomeViewModel
import com.ribod.kdraw.ui.main.tabs.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::SettingsViewModel)
}