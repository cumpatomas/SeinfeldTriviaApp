package com.cumpatomas.seinfeldrecords.data.database

import androidx.room.Room


class CharRecordRepository (

    private val db: RecordDatabase = Room
        .databaseBuilder(
            ApplicationModule.applicationContext,
            RecordDatabase::class.java,
            "record-database"
        )
        .build()
    )
{
    // suspend fun getRecord(): List<CharRecord> = db.recordDao().getRecord()
//    suspend fun getRecordbyId(id: Int): CharRecord = db.recordDao().getRecord(id)


}