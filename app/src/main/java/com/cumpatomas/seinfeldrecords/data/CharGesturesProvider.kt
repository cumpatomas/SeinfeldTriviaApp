package com.cumpatomas.seinfeldrecords.data

import com.cumpatomas.seinfeldrecords.data.model.CharGestures

class CharGesturesProvider() {
    private val listKramer = listOf<CharGestures>(
        CharGestures(
            id = "Kramer1",
            char = "Kramer",
            audioLink = "https://seinfeldapp-29d5f.web.app/audios/Kramer/Kramer1.mp3",
            photoLink = "https://seinfeldapp-29d5f.web.app/audios/Kramer/Kramer1.jpg"
        ),
        CharGestures(
            id = "Kramer2",
            char = "Kramer",
            audioLink = "https://seinfeldapp-29d5f.web.app/audios/Kramer/Kramer2.mp3",
            photoLink = "https://seinfeldapp-29d5f.web.app/audios/Kramer/Kramer2.jpg"
        ),
        CharGestures(
            id = "Kramer3",
            char = "Kramer",
            audioLink = "https://seinfeldapp-29d5f.web.app/audios/Kramer/Kramer1.mp3",
            photoLink = "https://seinfeldapp-29d5f.web.app/audios/Kramer/Kramer1.jpg"
        ),
        CharGestures(
            id = "Kramer4",
            char = "Kramer",
            audioLink = "https://seinfeldapp-29d5f.web.app/audios/Kramer/Kramer2.mp3",
            photoLink = "https://seinfeldapp-29d5f.web.app/audios/Kramer/Kramer2.jpg"
        ),
    )
    private val listEstelle = listOf<CharGestures>(
        CharGestures(
            id = "Estelle1",
            char = "Kramer",
            audioLink = "https://seinfeldapp-29d5f.web.app/audios/Estelle/Estelle1.mp3",
            photoLink = "https://seinfeldapp-29d5f.web.app/audios/Estelle/Estelle1.jpg"
        ),
        CharGestures(
            id = "Estelle2",
            char = "Kramer",
            audioLink = "https://seinfeldapp-29d5f.web.app/audios/Estelle/Estelle2.mp3",
            photoLink = "https://seinfeldapp-29d5f.web.app/audios/Estelle/Estelle2.jpg"
        ),
        CharGestures(
            id = "Estelle3",
            char = "Kramer",
            audioLink = "https://seinfeldapp-29d5f.web.app/audios/Estelle/Estelle3.mp3",
            photoLink = "https://seinfeldapp-29d5f.web.app/audios/Estelle/Estelle3.jpg"
        ),
        CharGestures(
            id = "Estelle4",
            char = "Kramer",
            audioLink = "https://seinfeldapp-29d5f.web.app/audios/Estelle/Estelle4.mp3",
            photoLink = "https://seinfeldapp-29d5f.web.app/audios/Estelle/Estelle4.jpg"
        ),
        )

    operator fun invoke(char: String): List<CharGestures> {
        return when (char) {
            "Kramer" -> listKramer
            "Estelle" -> listEstelle
            else -> emptyList()
        }
    }
}

