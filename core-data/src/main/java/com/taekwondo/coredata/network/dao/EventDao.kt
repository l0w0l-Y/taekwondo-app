package com.taekwondo.coredata.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.taekwondo.coredata.network.entity.EventEntity

@Dao
interface EventDao {
    @Insert
    fun insert(entity: EventEntity)

    @Query("SELECT * FROM event")
    fun getAllEvents(): List<EventEntity>
}