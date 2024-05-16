package com.taekwondo.coredata.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fighter")
data class FighterEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "fighterId")
    val uid: Int = 0,
    @ColumnInfo(name = "name")
    val name:String,
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