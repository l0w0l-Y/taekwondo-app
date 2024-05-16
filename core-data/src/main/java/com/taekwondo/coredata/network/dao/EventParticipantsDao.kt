package com.taekwondo.coredata.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.taekwondo.coredata.network.entity.EventFighterCrossRef
import com.taekwondo.coredata.network.entity.EventJudgeCrossRef
import com.taekwondo.coredata.network.entity.EventParticipants

@Dao
interface EventParticipantsDao {
    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    fun getEventParticipants(eventId: Int): EventParticipants?

    @Insert
    fun insertEventJudgeCrossRef(crossRef: EventJudgeCrossRef)

    @Insert
    fun insertEventFighterCrossRef(crossRef: EventFighterCrossRef)
}