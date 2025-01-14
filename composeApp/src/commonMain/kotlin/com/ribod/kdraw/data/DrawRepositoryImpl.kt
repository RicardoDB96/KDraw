package com.ribod.kdraw.data

import com.ribod.kdraw.data.database.KDrawDatabase
import com.ribod.kdraw.domain.DrawRepository
import com.ribod.kdraw.domain.model.DrawModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class DrawRepositoryImpl(private val kDrawDatabase: KDrawDatabase) : DrawRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllDraws(): Flow<List<DrawModel>> {
        return kDrawDatabase.getDrawDao().getAllDraws().mapLatest { it.map { it.toDomain() } }
    }

    override suspend fun saveDrawDB() {
        kDrawDatabase.getDrawDao().insertDraw()
    }

    override suspend fun deleteDrawDB(ids: Set<Long>) {
        kDrawDatabase.getDrawDao().deleteDrawsAndRelatedLines(ids = ids)
    }

    override suspend fun updateDrawDB(drawModel: DrawModel) {
        TODO("Not yet implemented")
    }
}