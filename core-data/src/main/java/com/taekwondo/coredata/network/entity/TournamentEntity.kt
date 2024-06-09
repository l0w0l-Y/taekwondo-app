package com.taekwondo.coredata.network.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Сущность, представляющая турнир.
 * @param uid Идентификатор турнира.
 * @param eventId Идентификатор события.
 * @param fighterId1 Идентификатор первого бойца.
 * @param fighterId2 Идентификатор второго бойца.
 * @param round Раунд.
 * @param group Группа.
 * @param winnerIndex Идентификатор победителя.
 */
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