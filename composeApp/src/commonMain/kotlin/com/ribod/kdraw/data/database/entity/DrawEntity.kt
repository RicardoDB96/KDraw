package com.ribod.kdraw.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ribod.kdraw.domain.model.DrawModel

@Entity
data class DrawEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
) {
    fun toDomain(): DrawModel = DrawModel(id = id, name = name)
}
