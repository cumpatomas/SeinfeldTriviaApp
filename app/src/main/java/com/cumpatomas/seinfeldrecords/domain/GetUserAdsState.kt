package com.cumpatomas.seinfeldrecords.domain

import com.cumpatomas.seinfeldrecords.data.database.UserDao
import javax.inject.Inject

class GetUserAdsState@Inject constructor(
    private val userDao: UserDao
) {
    suspend operator fun invoke(): Boolean {
        return userDao.getUserAdsState()
    }

}