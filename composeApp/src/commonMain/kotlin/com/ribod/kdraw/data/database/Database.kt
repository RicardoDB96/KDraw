package com.ribod.kdraw.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.ribod.kdraw.data.database.dao.DrawDao
import com.ribod.kdraw.data.database.dao.LinesDao
import com.ribod.kdraw.data.database.entity.DrawEntity
import com.ribod.kdraw.data.database.entity.LinesEntity

const val DATABASE_NAME = "kdraw.db"

expect object KDrawCTor : RoomDatabaseConstructor<KDrawDatabase>

@Database(entities = [DrawEntity::class, LinesEntity::class], version = 1)
@TypeConverters(Converters::class)
@ConstructedBy(KDrawCTor::class)
abstract class KDrawDatabase : RoomDatabase() {
    // DAO's
    abstract fun getDrawDao(): DrawDao
    abstract fun getLinesDao(): LinesDao
}