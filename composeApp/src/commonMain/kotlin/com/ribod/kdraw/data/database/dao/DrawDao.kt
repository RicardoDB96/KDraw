package com.ribod.kdraw.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ribod.kdraw.data.database.entity.DrawEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrawDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraw(drawEntity: DrawEntity): Long

    @Transaction
    suspend fun insertDraw() {
        val id = insertDraw(DrawEntity(name = ""))
        insertDraw(DrawEntity(id = id, name = "Draw $id"))
    }

    @Query("SELECT * FROM DrawEntity")
    fun getAllDraws(): Flow<List<DrawEntity>>

    @Query("DELETE FROM DrawEntity WHERE id IN (:ids)")
    suspend fun deleteDrawsByIds(ids: Set<Long>)
}