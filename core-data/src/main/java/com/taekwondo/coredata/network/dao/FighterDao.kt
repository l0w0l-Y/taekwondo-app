package com.taekwondo.coredata.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.taekwondo.coredata.network.entity.FighterEntity


@Dao
interface FighterDao {
    @Insert
    fun insert(entity: FighterEntity)

    @Update
    fun update(entity: FighterEntity)

    @Query("SELECT * FROM fighter")
    fun getAllFighters(): List<FighterEntity>

    @Query("SELECT * FROM fighter WHERE fighterId = :uid")
    fun getFighter(uid: Long): FighterEntity?

    @Query("DELETE FROM fighter WHERE fighterId = :uid")
    fun deleteFighter(uid: Long)
}