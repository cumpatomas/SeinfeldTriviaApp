package com.cumpatomas.seinfeldrecords.domain

import com.cumpatomas.seinfeldrecords.data.database.LocalDatabaseModule
import com.cumpatomas.seinfeldrecords.data.database.entities.UserEntity

class GetUserPoints {
    suspend operator fun invoke(): Int {

        if (LocalDatabaseModule.db.userDao().checkIfUserExists().isEmpty()) {
            LocalDatabaseModule.db.userDao().InsertUserEntity(UserEntity())
        }
        return LocalDatabaseModule.db.userDao().getPoints()
    }
}