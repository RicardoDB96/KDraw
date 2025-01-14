package com.ribod.kdraw.domain

import com.ribod.kdraw.domain.model.GlobalLine
import kotlinx.coroutines.flow.Flow

interface LinesRepository {
    suspend fun getDrawDB(drawId: Long): Flow<List<GlobalLine>>
    suspend fun saveLineDB(line: GlobalLine)
    suspend fun deleteLinesDB(ids: Set<String>)
    suspend fun updateLinesDB(lines: List<GlobalLine>)
}