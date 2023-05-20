package com.cumpatomas.seinfeldrecords.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cumpatomas.seinfeldrecords.data.database.entities.RecordEntity
import com.cumpatomas.seinfeldrecords.data.model.CharRecord

@Dao
interface RecordDao {
    @Insert
    suspend fun insertRecordList(recordList: List<RecordEntity>)
    @Query("SELECT * FROM record_entity")
    suspend fun getRecord(): List<RecordEntity>
    @Query("SELECT * FROM record_entity WHERE id=(:id)")
    suspend fun getRecord(id: Int): CharRecord
    @Query("DELETE FROM record_entity")
    fun deleteRecordList()
}