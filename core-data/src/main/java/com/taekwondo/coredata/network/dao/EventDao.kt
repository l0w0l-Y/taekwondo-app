package com.taekwondo.coredata.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.taekwondo.coredata.network.entity.EventEntity
import com.taekwondo.coredata.network.entity.FighterEntity

@Dao
interface EventDao {
    @Insert
    fun insert(entity: EventEntity)

    @Update
    fun update(entity: EventEntity)

    @Query("SELECT * FROM event")
    fun getAllEvents(): List<EventEntity>

    @Query("SELECT * FROM event WHERE eventId = :uid")
    fun getEvent(uid: Int): EventEntity?
}