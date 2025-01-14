package com.ribod.kdraw.data

import com.ribod.kdraw.data.database.KDrawDatabase
import com.ribod.kdraw.domain.LinesRepository
import com.ribod.kdraw.domain.model.GlobalLine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LinesRepositoryImpl(kDrawDatabase: KDrawDatabase): LinesRepository {

    private val linesDao = kDrawDatabase.getLinesDao()

    override suspend fun getDrawDB(drawId: Long): Flow<List<GlobalLine>> {
        return linesDao.getDraw(drawId).map { it.lines.map { it.toDomain() } }
    }

    override suspend fun saveLineDB(line: GlobalLine) {
        linesDao.insertLine(line.toEntity())
    }

    override suspend fun deleteLinesDB(ids: Set<String>) {
        linesDao.deleteLinesByIds(ids)
    }

    override suspend fun updateLinesDB(lines: List<GlobalLine>) {
        linesDao.updateGlobalLines(lines.map { it.toEntity() })
    }
}