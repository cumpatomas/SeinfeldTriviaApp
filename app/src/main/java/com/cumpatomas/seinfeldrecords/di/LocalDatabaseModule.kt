package com.cumpatomas.seinfeldrecords.di

import androidx.room.Room
import com.cumpatomas.seinfeldrecords.data.database.ApplicationModule
import com.cumpatomas.seinfeldrecords.data.database.GestureDao
import com.cumpatomas.seinfeldrecords.data.database.LocalDataBase
import com.cumpatomas.seinfeldrecords.data.database.QuestionDao
import com.cumpatomas.seinfeldrecords.data.database.UserDao
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

        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(db: LocalDataBase): UserDao {
        return db.userDao()
    }

    @Provides
    fun provideGestureDao(db: LocalDataBase): GestureDao {
        return db.GestureDao()
    }

    @Provides
    fun provideQuestion(db: LocalDataBase): QuestionDao {
        return db.questionDao()
    }

}


