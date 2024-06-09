package com.taekwondo.coredata.network.entity

import androidx.room.Entity
import androidx.room.ForeignKey

/** Сущность, представляющая бой.
 * @param tournamentId Идентификатор турнира.
 * @param eventId Идентификатор события.
 * @param judgeId Идентификатор судьи.
 * @param fighterId1 Идентификатор первого бойца.
 * @param fighterId2 Идентификатор второго бойца.
 * @param points1 Очки первого бойца.
 * @param points2 Очки второго бойца.
 */
@Entity(
    tableName = "fight", foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = JudgeEntity::class,
            parentColumns = ["judgeId"],
            childColumns = ["judgeId"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = FighterEntity::class,
            parentColumns = ["fighterId"],
            childColumns = ["fighterId1"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = FighterEntity::class,
            parentColumns = ["fighterId"],
            childColumns = ["fighterId2"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = TournamentEntity::class,
            parentColumns = ["uid"],
            childColumns = ["tournamentId"],
            onDelete = ForeignKey.NO_ACTION
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