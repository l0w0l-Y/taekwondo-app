package com.taekwondo.coredata.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.taekwondo.coredata.network.entity.FighterEntity


@Dao
interface FighterDao {
    /**
     * Вставляет новый FighterEntity в базу данных.
     * @param entity FighterEntity, который нужно вставить.
     */
    @Insert
    fun insert(entity: FighterEntity)

    /**
     * Вставляет список FighterEntity в базу данных.
     * @param entities Список FighterEntity, который нужно вставить.
     */
    @Insert
    fun insertAll(entities: List<FighterEntity>)

    /**
     * Обновляет FighterEntity в базе данных.
     * @param entity FighterEntity, который нужно обновить.
     */
    @Update
    fun update(entity: FighterEntity)

    /**
     * Получает всех FighterEntity из базы данных.
     * @return Список всех FighterEntity.
     */
    @Query("SELECT * FROM fighter")
    fun getAllFighters(): List<FighterEntity>

    /**
     * Получает FighterEntity по идентификатору.
     * @param uid Идентификатор FighterEntity.
     * @return FighterEntity с указанным идентификатором или null.
     */
    @Query("SELECT * FROM fighter WHERE fighterId = :uid")
    fun getFighter(uid: Long): FighterEntity?

    /**
     * Удаляет FighterEntity из базы данных.
     * @param uid Идентификатор FighterEntity.
     */
    @Query("DELETE FROM fighter WHERE fighterId = :uid")
    fun deleteFighter(uid: Long)
}