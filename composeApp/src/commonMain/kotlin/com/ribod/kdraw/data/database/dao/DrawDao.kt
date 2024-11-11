package com.ribod.kdraw.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ribod.kdraw.data.database.entity.DrawEntity

@Dao
interface DrawDao {

    @Query("SELECT * FROM DrawEntity")
    suspend fun getAllDraw(): List<DrawEntity>
}