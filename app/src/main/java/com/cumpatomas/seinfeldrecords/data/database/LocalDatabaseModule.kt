package com.cumpatomas.seinfeldrecords.data.database

import androidx.room.Room

object LocalDatabaseModule {

    val db: RecordDatabase by lazy {
        provideDatabase()
    }

    private fun provideDatabase(): RecordDatabase {
        return Room.databaseBuilder(ApplicationModule.applicationContext, RecordDatabase::class.java, "local_db").build()
    }
}


