package com.cumpatomas.seinfeldrecords.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cumpatomas.seinfeldrecords.data.database.entities.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insertUserEntity(user: UserEntity)

    @Query("SELECT * FROM user_entity")
    suspend fun checkIfUserExists(): List<UserEntity>

    @Query("SELECT points FROM user_entity")
    suspend fun getPoints(): Int


    @Query("UPDATE user_entity SET points = :points ")
    suspend fun updatePoints(points: Int): Int


}