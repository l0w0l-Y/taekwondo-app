package com.taekwondo.coredata.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @ColumnInfo(name = "photo")
    val photo: String?,
)