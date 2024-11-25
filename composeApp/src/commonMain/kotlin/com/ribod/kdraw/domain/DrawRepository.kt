package com.ribod.kdraw.domain

import com.ribod.kdraw.domain.model.DrawModel
import kotlinx.coroutines.flow.Flow

interface DrawRepository {
    suspend fun getAllDraws(): Flow<List<DrawModel>>
    suspend fun saveDrawDB()
    suspend fun deleteDrawDB(ids: Set<Long>)
    suspend fun updateDrawDB(drawModel: DrawModel)
}