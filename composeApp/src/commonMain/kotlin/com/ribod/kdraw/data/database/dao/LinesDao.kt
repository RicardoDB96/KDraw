package com.ribod.kdraw.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ribod.kdraw.data.database.entity.DrawWithLines
import com.ribod.kdraw.data.database.entity.LinesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LinesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLine(line: LinesEntity)

    @Transaction
    @Query("SELECT * FROM DrawEntity WHERE id = :drawId")
    fun getDraw(drawId: Long): Flow<DrawWithLines>

    @Query("DELETE FROM LinesEntity WHERE id IN (:lineIds)")
    suspend fun deleteLinesByIds(lineIds: Set<String>)

    @Update
    suspend fun updateGlobalLines(globalLines: List<LinesEntity>)
}