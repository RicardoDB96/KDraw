package com.ribod.kdraw.ui.main.tabs.home

import com.ribod.kdraw.domain.model.DrawModel

data class HomeState(
    val draws: List<DrawModel> = emptyList(),
    val selectedDraws: Set<Long> = emptySet(),
)