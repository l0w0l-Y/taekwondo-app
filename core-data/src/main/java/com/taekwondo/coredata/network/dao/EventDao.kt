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
import com.taekwondo.coredata.network.entity.TournamentEntity

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

    /**
     * Добавляет новый FightEntity в базу данных или обновляет существующий.
     * @param fightEntity FightEntity, который нужно добавить или обновить.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFightEntity(fightEntity: FightEntity)

    /**
     * Обновляет FightEntity в базе данных.
     * @param fight FightEntity, который нужно обновить.
     */
    @Update
    fun updateFight(fight: FightEntity)

    /**
     * Получает FightEntity по идентификатору.
     * @param uid Идентификатор FightEntity.
     * @return FightEntity с указанным идентификатором или null.
     */
    @Query("SELECT * FROM fight WHERE fighterId1 = :fightId1 AND fighterId2 = :fightId2 AND judgeId = :judgeId AND eventId = :eventId")
    fun getFight(eventId: Long, judgeId: Long, fightId1: Long, fightId2: Long): FightEntity?

    /**
     * Получает FightEntity по идентификатору турнира.
     * @param uid Идентификатор турнира.
     * @return список FightEntity.
     */
    @Query("SELECT * FROM fight WHERE tournamentId = :uid")
    fun getFightsByTournamentId(uid: Long): List<FightEntity>

    /**
     * Обновляет FightEntity или создает новый в базе данных.
     * @param tournamentId Идентификатор турнира.
     * @param judgeId Идентификатор судьи.
     * @param eventId Идентификатор события.
     * @param fighterId1 Идентификатор первого бойца.
     * @param fighterId2 Идентификатор второго бойца.
     * @param points1 Очки первого бойца.
     * @param points2 Очки второго бойца.
     */
    @Transaction
    fun upsertFight(
        tournamentId: Long,
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
                    tournamentId,
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
                    tournamentId,
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

    /**
     * Получает все FightEntity из базы данных.
     * @return Список всех FightEntity.
     */
    @Query("SELECT * FROM fight")
    fun getAllFights(): List<FightEntity>

    /**
     * Получает все FightEntity по идентификатору события.
     * @param eventId Идентификатор события.
     * @return список FightEntity.
     */
    @Query("SELECT * FROM fight WHERE eventId = :eventId")
    fun getFightsByEventId(eventId: Long): List<FightEntity>

    /**
     * Удаляет EventEntity из базы данных.
     * @param uid Идентификатор EventEntity.
     */
    @Query("DELETE FROM event WHERE eventId = :uid")
    fun deleteEvent(uid: Long)

    /**
     * Получает все ResultEntity по идентификатору события.
     * @param eventId Идентификатор события.
     * @return список ResultEntity.
     */
    @Query("SELECT * FROM result WHERE eventId = :eventId")
    fun getAllResults(eventId: Long): List<ResultEntity>

    /**
     * Добавляет новый ResultEntity в базу данных.
     * @param resultEntity ResultEntity, который нужно добавить.
     */
    @Insert
    fun insertResult(resultEntity: ResultEntity)

    /**
     * Обновляет ResultEntity в базе данных.
     * @param resultEntity ResultEntity, который нужно обновить.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertResult(resultEntity: ResultEntity)

    /**
     * Получает турнир по идентификатору события.
     * @param eventId Идентификатор события.
     * @return список TournamentEntity.
     */
    @Query("SELECT * FROM tournament WHERE eventId = :eventId")
    fun getTournamentEvent(eventId: Long): List<TournamentEntity>

    /**
     * Получает турнир по идентификатору.
     * @param tournamentId Идентификатор турнира.
     * @return TournamentEntity.
     */
    @Query("SELECT * FROM tournament WHERE uid = :tournamentId")
    fun getTournament(tournamentId: Long): TournamentEntity?

    /**
     * Получает турнир по идентификатору события,раунду и группе.
     * @param eventId Идентификатор события.
     * @param round Раунд.
     * @param group Группа.
     * @return TournamentEntity.
     */
    @Query("SELECT * FROM tournament WHERE eventId = :eventId AND round = :round AND `group` = :group")
    fun getTournament(eventId: Long, round: Int, group: Int): TournamentEntity?

    /**
     * Добавляет новый TournamentEntity в базу данных.
     * @param tournament TournamentEntity, который нужно добавить.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTournament(tournament: TournamentEntity)

    /**
     * Получает идентификатор главного судьи по идентификатору события.
     * @param eventId Идентификатор события.
     */
    @Query("SELECT judgeId FROM EventMainJudgeCrossRef WHERE eventId = :eventId")
    fun getMainJudge(eventId: Long): Long?
}