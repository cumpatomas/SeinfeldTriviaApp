package com.cumpatomas.seinfeldrecords.data

class RandomResponseText {

    fun getAnswer(isCorrect: Boolean): String {
        return if (!isCorrect) {
            listOf<String>(
                "You're not Penske material!",
                "Why can't you be more like Lloyd Braun?",
                "Sweet fancy Moses!",
                "Stuff your sorries in a sack mister!",
                "That's a shame",
            ).shuffled().random()
        } else {
            listOf<String>(
                "Correct! Oh you are something!",
                "Correct! How can anyone not like you?",
                "Correct! Giddy Up!",
                "Correct! High five!",
            ).shuffled().random()
        }

    }

}