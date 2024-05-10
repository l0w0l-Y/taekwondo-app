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

    @Query("SELECT * FROM fighter WHERE uid = :uid")
    fun getFighter(uid: Int): FighterEntity?

    @Query("DELETE FROM fighter WHERE uid = :uid")
    fun deleteFighter(uid: Int)
}