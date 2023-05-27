package com.cumpatomas.seinfeldrecords.data.database

import androidx.room.Room

object LocalDatabaseModule {

    val db: UserDataBase by lazy {
        provideDatabase()
    }

    private fun provideDatabase(): UserDataBase {
        return Room.databaseBuilder(ApplicationModule.applicationContext, UserDataBase::class.java, "local_db").build()
    }
}


