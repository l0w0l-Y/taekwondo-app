package com.taekwondo.coredata.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность, представляющая судью.
 * @param uid Идентификатор судьи.
 * @param name Имя судьи.
 * @param username Логин судьи.
 * @param email Почта судьи.
 * @param password Пароль судьи.
 * @param phone Телефон судьи.
 * @param photo Фотография судьи.
 */
@Entity(tableName = "judge")
data class JudgeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "judgeId")
    val uid: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "phone")
    val phone: String,
    @ColumnInfo(name = "photo")
    val photo: String,
)