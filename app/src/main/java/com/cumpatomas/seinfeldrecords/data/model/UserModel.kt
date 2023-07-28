package com.cumpatomas.seinfeldrecords.data.model

import com.cumpatomas.seinfeldrecords.data.database.entities.UserEntity

data class UserModel(
        val id: Int? = null,
        var name: String? = null,
        var points: Int = 0,
        var noAds: Boolean = false
    )

    fun UserEntity.toDomain() = UserModel(
        id = id ?: 0,
        name = name,
        points = points,
        noAds = noAds
    )
