package com.taekwondo.coredata.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.taekwondo.coredata.network.entity.JudgeEntity

@Dao
interface AuthDao {
    @Insert
    fun insert(entity: JudgeEntity) : Long

    @Query("SELECT * FROM judge WHERE email LIKE :email LIMIT 1")
    fun getUserByEmail(email: String): JudgeEntity?

    @Query("SELECT * FROM judge")
    fun getAllUsers(): List<JudgeEntity>
}
