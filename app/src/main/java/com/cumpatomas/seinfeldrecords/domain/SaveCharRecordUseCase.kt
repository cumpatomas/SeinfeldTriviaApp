package com.cumpatomas.seinfeldrecords.domain

import com.cumpatomas.seinfeldrecords.data.CharRecordProvider

class SaveCharRecordUseCase {

    private val provider = CharRecordProvider()

/*    suspend operator fun invoke() {
        val recordList: List<CharRecord> = provider.getCharRecord()
        val recordListEntity: List<RecordEntity> = recordList.map { it.toEntity() }

        LocalDatabaseModule.db.recordDao().deleteRecordList()
        LocalDatabaseModule.db.recordDao().insertRecordList(recordListEntity)
    }*/
}