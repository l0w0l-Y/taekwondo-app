package com.taekwondo.coredata.network.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

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
    val fighters: List<FighterEntity>
)

@Entity(primaryKeys = ["eventId", "judgeId"])
data class EventJudgeCrossRef(
    val eventId: Long,
    val judgeId: Long
)

@Entity(primaryKeys = ["eventId", "fighterId"])
data class EventFighterCrossRef(
    val eventId: Long,
    val fighterId: Long
)