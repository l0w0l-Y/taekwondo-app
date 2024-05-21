package com.taekwondo.coredata.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.taekwondo.coredata.network.entity.EventEntity
import com.taekwondo.coredata.network.entity.FightEntity
import com.taekwondo.coredata.network.entity.ResultEntity

@Dao
interface EventDao {
    /**
     * Вставляет новый EventEntity в базу данных.
     * @param entity EventEntity, который нужно вставить.
     */
    @Insert
    fun insert(entity: EventEntity)

    /**
     * Обновляет EventEntity в базе данных.
     * @param entity EventEntity, который нужно обновить.
     */
    @Update
    fun update(entity: EventEntity)

    /**
     * Получает все EventEntity из базы данных.
     * @return Список всех EventEntity.
     */
    @Query("SELECT * FROM event")
    fun getAllEvents(): List<EventEntity>

    /**
     * Получает EventEntity по идентификатору.
     * @param uid Идентификатор EventEntity.
     * @return EventEntity с указанным идентификатором или null.
     */
    @Query("SELECT * FROM event WHERE eventId = :uid")
    fun getEvent(uid: Long): EventEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFightEntity(fightEntity: FightEntity)

    @Update
    fun updateFight(fight: FightEntity)

    @Query("SELECT * FROM fight WHERE fighterId1 = :fightId1 AND fighterId2 = :fightId2 AND judgeId = :judgeId AND eventId = :eventId")
    fun getFight(eventId: Long, judgeId: Long, fightId1: Long, fightId2: Long): FightEntity?

    @Transaction
    fun upsertFight(
        judgeId: Long,
        eventId: Long,
        fighterId1: Long,
        fighterId2: Long,
        points1: Int,
        points2: Int
    ) {
        val fight = getFight(eventId, judgeId, fighterId1, fighterId2)
        if (fight != null) {
            insertFightEntity(
                FightEntity(
                    eventId,
                    judgeId,
                    fighterId1,
                    fighterId2,
                    fight.points1 + points1,
                    fight.points2 + points2
                )
            )
        } else {
            insertFightEntity(
                FightEntity(
                    eventId,
                    judgeId,
                    fighterId1,
                    fighterId2,
                    points1,
                    points2
                )
            )
        }
    }

    @Query("SELECT * FROM fight")
    fun getAllFights(): List<FightEntity>

    @Query("SELECT * FROM fight WHERE eventId = :eventId")
    fun getFightsByEventId(eventId: Long): List<FightEntity>

    @Query("DELETE FROM event WHERE eventId = :uid")
    fun deleteEvent(uid: Long)

    @Query("SELECT * FROM result WHERE eventId = :eventId")
    fun getAllResults(eventId: Long): List<ResultEntity>

    @Insert()
    fun insertResult(resultEntity: ResultEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertResult(resultEntity: ResultEntity)
}