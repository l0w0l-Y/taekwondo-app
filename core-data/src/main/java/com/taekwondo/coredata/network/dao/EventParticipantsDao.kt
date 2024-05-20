package com.taekwondo.coredata.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.taekwondo.coredata.network.entity.EventFighterCrossRef
import com.taekwondo.coredata.network.entity.EventJudgeCrossRef
import com.taekwondo.coredata.network.entity.EventParticipants
import com.taekwondo.coredata.network.entity.FighterEntity

@Dao
interface EventParticipantsDao {
    /**
     * Вставляет новый EventParticipants в базу данных. Если уже есть, заменяет.
     * @param entity EventParticipants, который нужно вставить.
     */
    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    fun getEventParticipants(eventId: Long): EventParticipants?

    /**
     * Вставляет новый EventJudgeCrossRef (отношение eventId и judgeId) в базу данных. Если уже есть, заменяет.
     * @param entity EventJudgeCrossRef, который нужно вставить.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEventJudgeCrossRef(crossRef: EventJudgeCrossRef)

    /**
     * Вставляет новый EventFighterCrossRef (отношение eventId и fighterId) в базу данных. Если уже есть, заменяет.
     * @param entity EventFighterCrossRef, который нужно вставить.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEventFighterCrossRef(crossRef: EventFighterCrossRef)

    /**
     * Удаляет EventFighterCrossRef (отношение eventId и fighterId) из базы данных.
     * @param eventId Идентификатор события.
     */
    @Query("DELETE FROM eventfightercrossref WHERE eventId = :eventId")
    fun removeEventFighterCrossRef(eventId: Long)

    /**
     * Удаляет EventJudgeCrossRef (отношение eventId и judgeId) из базы данных.
     * @param eventId Идентификатор события.
     */
    @Query("DELETE FROM eventjudgecrossref WHERE eventId = :eventId")
    fun removeEventJudgeCrossRef(eventId: Long)

    @Query("SELECT * FROM fighter WHERE fighterId IN (SELECT fighterId FROM eventfightercrossref WHERE eventId = :eventId)")
    fun getFightersEvent(eventId: Long): List<FighterEntity>
}