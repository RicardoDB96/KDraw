package com.ribod.kdraw.domain.model

import com.ribod.kdraw.data.database.entity.DrawEntity

data class DrawModel(
    val id: Long,
    val name: String,
) {
    fun toEntity(): DrawEntity = DrawEntity(id = id, name = name)
}
