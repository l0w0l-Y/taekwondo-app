package com.taekwondo.coredata.network.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "result")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventId: Long,
    val winner: Long,
    val loser: Long,
)