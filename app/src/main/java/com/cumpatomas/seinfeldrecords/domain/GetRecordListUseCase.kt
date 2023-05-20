package com.cumpatomas.seinfeldrecords.domain

import com.cumpatomas.seinfeldrecords.data.database.LocalDatabaseModule
import com.cumpatomas.seinfeldrecords.data.model.CharRecord
import com.cumpatomas.seinfeldrecords.data.model.toDomain

class GetRecordListUseCase {

    suspend operator fun invoke(): List<CharRecord> {
        return LocalDatabaseModule.db.recordDao().getRecord().map { it.toDomain() }
    }

}