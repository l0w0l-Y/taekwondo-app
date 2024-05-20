package com.taekwondo.coredata.network.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "fight", foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = JudgeEntity::class,
            parentColumns = ["judgeId"],
            childColumns = ["judgeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FighterEntity::class,
            parentColumns = ["fighterId"],
            childColumns = ["fighterId"],
            onDelete = ForeignKey.CASCADE
        )
    ], primaryKeys = ["eventId", "judgeId", "fighterId", "round"]
)
data class FightEntity(
    val eventId: Long,
    val judgeId: Long,
    val fighterId: Long,
    val points: Int,
    val round: Int,
)