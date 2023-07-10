package com.cumpatomas.seinfeldrecords.domain

import com.cumpatomas.seinfeldrecords.data.database.GestureDao
import com.cumpatomas.seinfeldrecords.data.database.entities.GestureEntity
import javax.inject.Inject


class InsertGesturesInDataBase@Inject constructor(
    private val gesturesDao: GestureDao
) {
    suspend operator fun invoke() {
        val gesturesList = gesturesDao.getGestureList()
        if (gesturesList.isEmpty()) {
            gesturesDao.insertGesture(GestureEntity("Jerry"))
            gesturesDao.insertGesture(GestureEntity("George"))
            gesturesDao.insertGesture(GestureEntity("Elaine"))
            gesturesDao.insertGesture(GestureEntity("Kramer"))
            gesturesDao.insertGesture(GestureEntity("Estelle"))
            gesturesDao.insertGesture(GestureEntity("Frank"))
            gesturesDao.insertGesture(GestureEntity("Newman"))
            gesturesDao.insertGesture(GestureEntity("Puddy"))
        }
    }
}