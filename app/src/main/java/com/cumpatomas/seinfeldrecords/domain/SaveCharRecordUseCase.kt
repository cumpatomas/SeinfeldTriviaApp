package com.cumpatomas.seinfeldrecords.domain

import com.cumpatomas.seinfeldrecords.data.CharRecordProvider
import com.cumpatomas.seinfeldrecords.data.database.LocalDatabaseModule
import com.cumpatomas.seinfeldrecords.data.database.entities.RecordEntity
import com.cumpatomas.seinfeldrecords.data.database.entities.toEntity
import com.cumpatomas.seinfeldrecords.data.model.CharRecord

class SaveCharRecordUseCase {

    private val provider = CharRecordProvider()

/*    suspend operator fun invoke() {
        val recordList: List<CharRecord> = provider.getCharRecord()
        val recordListEntity: List<RecordEntity> = recordList.map { it.toEntity() }

        LocalDatabaseModule.db.recordDao().deleteRecordList()
        LocalDatabaseModule.db.recordDao().insertRecordList(recordListEntity)
    }*/
}