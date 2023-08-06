package com.cumpatomas.seinfeldrecords.domain

import com.cumpatomas.seinfeldrecords.data.database.UserDao
import javax.inject.Inject

class SetNoAdsForUser @Inject constructor(
    private val userDao: UserDao
) {
    suspend operator fun invoke() {
        userDao.updateAds(true)
    }
}