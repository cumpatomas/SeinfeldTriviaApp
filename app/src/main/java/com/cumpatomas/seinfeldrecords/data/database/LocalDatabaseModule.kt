package com.cumpatomas.seinfeldrecords.data.database

import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object LocalDatabaseModule {
        val db: LocalDataBase by lazy {
            provideDatabase()
        }
    @Provides
    fun provideDatabase(): LocalDataBase {
        return Room.databaseBuilder(
            ApplicationModule.applicationContext,
            LocalDataBase::class.java,
            "local_db"
        ).build()
    }

    @Provides
    fun provideUserDao(db: LocalDataBase): UserDao {
        return db.userDao()
    }

}


