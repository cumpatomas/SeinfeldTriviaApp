package com.cumpatomas.seinfeldrecords.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cumpatomas.seinfeldrecords.data.database.entities.UserEntity

@Database(entities = [ UserEntity::class ], version=1, exportSchema = false)
@TypeConverters(RecordConverters::class)
abstract class UserDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao

}