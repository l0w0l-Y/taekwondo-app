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
 * @param city Город проведения события.
 * @param level Уровень события.
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
    @ColumnInfo(name = "city")
    val city: String,
    @ColumnInfo(name = "level")
    val level: EventLevel,
    @ColumnInfo(name = "place")
    val place: String,
    @ColumnInfo(name = "type")
    val status: EventStatus = EventStatus.IN_PROGRESS,
)

/**
 * Перечисление уровней события.
 */
enum class EventLevel(val value: String) {
    RUSSIAN("Всероссийский"),
    FEDERAL("Федеральный"),
    REGIONAL("Областной"),
    CITY("Городской"),
    DISTRICT("Районный"),
    AMATEUR("Любительский"),
}

/**
 * Перечисление статусов события.
 */
enum class EventStatus {
    IN_PROGRESS,
    FINISHED,
}