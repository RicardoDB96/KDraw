package com.ribod.kdraw.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.ribod.kdraw.domain.model.DrawModel

@Entity
data class DrawEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
) {
    fun toDomain(): DrawModel = DrawModel(id = id, name = name)
}

data class DrawWithLines(
    @Embedded val draw: DrawEntity,
    @Relation(
        parentColumn = "id", // Column DrawEntity
        entityColumn = "drawId" // Column LinesEntity
    )
    val lines: List<LinesEntity>
)