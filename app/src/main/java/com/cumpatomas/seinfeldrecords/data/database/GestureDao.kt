package com.cumpatomas.seinfeldrecords.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cumpatomas.seinfeldrecords.data.database.entities.GestureEntity

@Dao
interface GestureDao {

    @Insert
    suspend fun insertGesture(gesture: GestureEntity)

    @Query("SELECT * FROM gesture_entity")
    suspend fun getGestureList(): List<GestureEntity>

    @Query("UPDATE gesture_entity SET completed = :completed WHERE charName = :charName")
    suspend fun updateGesture(charName: String, completed: Boolean)

}