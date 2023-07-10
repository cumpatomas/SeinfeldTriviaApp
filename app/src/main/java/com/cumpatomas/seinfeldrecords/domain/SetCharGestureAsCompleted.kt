package com.cumpatomas.seinfeldrecords.domain

import com.cumpatomas.seinfeldrecords.data.database.GestureDao
import javax.inject.Inject

class SetCharGestureAsCompleted@Inject constructor(
    private val gestureDao: GestureDao
) {

    suspend operator fun invoke(char: String) {
        gestureDao.updateGesture(char, true)

    }
}