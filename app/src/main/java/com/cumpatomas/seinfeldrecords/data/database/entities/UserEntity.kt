package com.cumpatomas.seinfeldrecords.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cumpatomas.seinfeldrecords.data.model.UserModel


@Entity(tableName = "user_entity")
    data class UserEntity(
        @PrimaryKey(autoGenerate = true) val id: Int? = null,
        var name: String? = null,
        var points: Int = 0,
        var noAds: Boolean = false
    )

    fun UserModel.toEntity() = UserEntity(
        name = name,
        points = points,
        noAds = noAds
    )