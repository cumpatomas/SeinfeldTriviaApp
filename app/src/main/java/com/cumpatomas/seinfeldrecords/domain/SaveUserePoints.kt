package com.cumpatomas.seinfeldrecords.domain

import com.cumpatomas.seinfeldrecords.data.database.LocalDatabaseModule
import com.cumpatomas.seinfeldrecords.data.database.entities.UserEntity

class SaveUserPoints {
    suspend operator fun invoke(points: Int) {

LocalDatabaseModule.db.userDao().updatePoints(points)
    }
}