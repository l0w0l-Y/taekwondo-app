package com.taekwondo.coredata.network.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.taekwondo.coredata.network.dao.AuthDao
import com.taekwondo.coredata.network.dao.EventDao
import com.taekwondo.coredata.network.dao.EventParticipantsDao
import com.taekwondo.coredata.network.dao.FighterDao
import com.taekwondo.coredata.network.entity.EventEntity
import com.taekwondo.coredata.network.entity.EventFighterCrossRef
import com.taekwondo.coredata.network.entity.EventJudgeCrossRef
import com.taekwondo.coredata.network.entity.FighterEntity
import com.taekwondo.coredata.network.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        FighterEntity::class,
        EventEntity::class,
        EventJudgeCrossRef::class,
        EventFighterCrossRef::class],
    version = 16,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun authDao(): AuthDao
    abstract fun fighterDao(): FighterDao
    abstract fun eventDao(): EventDao
    abstract fun eventParticipantsDao(): EventParticipantsDao
}

class DatabaseError(message: String?, cause: Throwable? = null) : Exception(message, cause)
