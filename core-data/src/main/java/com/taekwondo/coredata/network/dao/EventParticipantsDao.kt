package com.taekwondo.coredata.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEventJudgeCrossRef(crossRef: EventJudgeCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEventFighterCrossRef(crossRef: EventFighterCrossRef)

    @Query("DELETE FROM eventfightercrossref WHERE eventId = :eventId")
    fun removeEventFighterCrossRef(eventId: Int)
    @Query("DELETE FROM eventjudgecrossref WHERE eventId = :eventId")
    fun removeEventJudgeCrossRef(eventId: Int)
}