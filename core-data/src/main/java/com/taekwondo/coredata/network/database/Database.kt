package com.taekwondo.coredata.network.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.taekwondo.coredata.network.dao.AuthDao
import com.taekwondo.coredata.network.dao.FighterDao
import com.taekwondo.coredata.network.entity.FighterEntity
import com.taekwondo.coredata.network.entity.UserEntity

@Database(entities = [UserEntity::class, FighterEntity::class], version = 4, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun authDao(): AuthDao
    abstract fun fighterDao(): FighterDao
}

class DatabaseError(message: String?, cause: Throwable? = null) : Exception(message, cause)
