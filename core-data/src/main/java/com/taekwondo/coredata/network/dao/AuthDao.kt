package com.taekwondo.coredata.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomWarnings
import com.taekwondo.coredata.network.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthDao {
    @Insert
    fun insert(entity: UserEntity)

    @Query("SELECT * FROM user WHERE email LIKE :email LIMIT 1")
    fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM user")
    fun getAllUsers(): List<UserEntity>
}
