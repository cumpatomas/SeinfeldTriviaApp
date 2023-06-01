package com.cumpatomas.seinfeldrecords.domain

import com.cumpatomas.seinfeldrecords.data.database.UserDao
import com.cumpatomas.seinfeldrecords.data.database.entities.UserEntity
import javax.inject.Inject

class GetUserPoints@Inject constructor(
    private val userDao: UserDao
) {
    suspend operator fun invoke(): Int {

        if (userDao.checkIfUserExists().isEmpty()) {
            userDao.InsertUserEntity(UserEntity())
        }
        return userDao.getPoints()
    }
}