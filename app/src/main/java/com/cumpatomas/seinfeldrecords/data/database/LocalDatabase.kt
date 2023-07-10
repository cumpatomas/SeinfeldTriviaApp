package com.cumpatomas.seinfeldrecords.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cumpatomas.seinfeldrecords.data.database.entities.GestureEntity
import com.cumpatomas.seinfeldrecords.data.database.entities.UserEntity

@Database(entities = [ UserEntity::class, GestureEntity::class ], version=2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun GestureDao(): GestureDao

}