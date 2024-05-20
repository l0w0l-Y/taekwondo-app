package com.taekwondo.coredata.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.taekwondo.coredata.network.entity.EventEntity
import com.taekwondo.coredata.network.entity.FightEntity

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
}