package com.ribod.kdraw.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.ribod.kdraw.data.database.dao.DrawDao
import com.ribod.kdraw.data.database.entity.DrawEntity

const val DATABASE_NAME = "kdraw.db"

expect object KDrawCTor : RoomDatabaseConstructor<KDrawDatabase>

@Database(entities = [DrawEntity::class], version = 1)
@ConstructedBy(KDrawCTor::class)
abstract class KDrawDatabase : RoomDatabase() {
    // DAO
    abstract fun getDrawDao(): DrawDao
}