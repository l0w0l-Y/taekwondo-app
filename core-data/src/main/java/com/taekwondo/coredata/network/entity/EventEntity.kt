package com.taekwondo.coredata.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность, представляющая событие.
 * @param uid Идентификатор события.
 * @param name Название события.
 * @param date Дата проведения события.
 * @param place Место проведения события.
 */
@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "eventId")
    val uid: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "place")
    val place: String,
)