package com.ribod.kdraw.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DrawEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
)
