package com.taekwondo.coredata.network.entity

import androidx.room.Entity
import androidx.room.ForeignKey

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
            childColumns = ["fighterId1"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FighterEntity::class,
            parentColumns = ["fighterId"],
            childColumns = ["fighterId2"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TournamentEntity::class,
            parentColumns = ["uid"],
            childColumns = ["tournamentId"],
            onDelete = ForeignKey.CASCADE
        )
    ], primaryKeys = ["tournamentId", "eventId", "judgeId", "fighterId1", "fighterId2"]
)
data class FightEntity(
    val tournamentId: Long,
    val eventId: Long,
    val judgeId: Long,
    val fighterId1: Long,
    val fighterId2: Long,
    val points1: Int,
    val points2: Int,
)