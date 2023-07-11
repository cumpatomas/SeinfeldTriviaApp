package com.cumpatomas.seinfeldrecords.domain

import com.cumpatomas.seinfeldrecords.data.database.UserDao
import javax.inject.Inject

const val MAX_POINTS = 1000

class SaveUserPoints @Inject constructor(
    private val userDao: UserDao
) {
    suspend operator fun invoke(points: Int) {

            userDao.updatePoints(points)
    }
}