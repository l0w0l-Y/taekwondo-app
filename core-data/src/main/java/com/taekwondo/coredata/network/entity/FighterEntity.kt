package com.taekwondo.coredata.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taekwondo.coredata.network.enums.Gender

/**
 * Сущность, представляющая бойца.
 * @param uid Идентификатор бойца.
 * @param name Имя бойца.
 * @param age Возраст бойца.
 * @param weight Вес бойца.
 * @param height Рост бойца.
 * @param weightCategory Весовая категория бойца.
 * @param photo Фотография бойца.
 */
@Entity(tableName = "fighter")
data class FighterEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "fighterId")
    val uid: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "age")
    val age: Float,
    @ColumnInfo(name = "weight")
    val weight: Float,
    @ColumnInfo(name = "height")
    val height: Float,
    @ColumnInfo(name = "weight_category")
    val weightCategory: String,
    @ColumnInfo(name = "club")
    val club: String,
    @ColumnInfo(name = "trainer")
    val trainer: String,
    @ColumnInfo(name = "gender")
    val gender: Gender,
    @ColumnInfo(name = "photo")
    val photo: String? = null,
)