package com.cumpatomas.seinfeldrecords.domain

import com.cumpatomas.seinfeldrecords.data.database.QuestionDao
import javax.inject.Inject

class UpdateAnsweredQuestion@Inject constructor(
    private val questionDao: QuestionDao
) {
    suspend operator fun invoke(question: String, answered: Boolean) {
        questionDao.updateQuestion(question, answered)
    }
}