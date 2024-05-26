package com.taekwondo.coredata.network.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tournament")
data class TournamentEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    val eventId: Long,
    val fighterId1: Long?,
    val fighterId2: Long?,
    val round: Int,
    val group: Int,
    val winnerIndex: Long?,
)