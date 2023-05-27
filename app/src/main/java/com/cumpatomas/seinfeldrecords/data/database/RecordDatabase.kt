package com.cumpatomas.seinfeldrecords.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cumpatomas.seinfeldrecords.data.database.entities.RecordEntity

@Database(entities = [ RecordEntity::class ], version=1, exportSchema = false)
@TypeConverters(RecordConverters::class)
    abstract class RecordDatabase : RoomDatabase() {

        abstract fun recordDao(): RecordDao

}