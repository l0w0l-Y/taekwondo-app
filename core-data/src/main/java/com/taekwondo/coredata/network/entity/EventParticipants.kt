package com.taekwondo.coredata.network.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

/**
 * Сущность, представляющая участников события.
 * @param event Событие.
 * @param judges Список судей.
 * @param fighters Список бойцов.
 */
class EventParticipants(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "judgeId",
        associateBy = Junction(EventJudgeCrossRef::class)
    )
    val judges: List<JudgeEntity>,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "fighterId",
        associateBy = Junction(EventFighterCrossRef::class)
    )
    val fighters: List<FighterEntity>,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "judgeId",
        associateBy = Junction(EventMainJudgeCrossRef::class)
    )
    val mainFighter: JudgeEntity?,

)

/**
 * Сущность, представляющая отношение между событием и судьей.
 */
@Entity(primaryKeys = ["eventId", "judgeId"])
data class EventJudgeCrossRef(
    val eventId: Long,
    val judgeId: Long
)

/**
 * Сущность, представляющая отношение между событием и бойцом.
 */
@Entity(primaryKeys = ["eventId", "fighterId"])
data class EventFighterCrossRef(
    val eventId: Long,
    val fighterId: Long
)

/**
 * Сущность, представляющая отношение между событием и главным судьей.
 */
@Entity(primaryKeys = ["eventId", "judgeId"])
data class EventMainJudgeCrossRef(
    val eventId: Long,
    val judgeId: Long
)