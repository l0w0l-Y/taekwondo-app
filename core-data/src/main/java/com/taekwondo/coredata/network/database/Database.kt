package com.taekwondo.coredata.network.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.taekwondo.coredata.network.dao.JudgeDao
import com.taekwondo.coredata.network.dao.EventDao
import com.taekwondo.coredata.network.dao.EventParticipantsDao
import com.taekwondo.coredata.network.dao.FighterDao
import com.taekwondo.coredata.network.entity.EventEntity
import com.taekwondo.coredata.network.entity.EventFighterCrossRef
import com.taekwondo.coredata.network.entity.EventJudgeCrossRef
import com.taekwondo.coredata.network.entity.EventMainJudgeCrossRef
import com.taekwondo.coredata.network.entity.FightEntity
import com.taekwondo.coredata.network.entity.FighterEntity
import com.taekwondo.coredata.network.entity.JudgeEntity
import com.taekwondo.coredata.network.entity.ResultEntity
import com.taekwondo.coredata.network.entity.TournamentEntity

@Database(
    entities = [
        JudgeEntity::class,
        FighterEntity::class,
        EventEntity::class,
        ResultEntity::class,
        EventJudgeCrossRef::class,
        EventFighterCrossRef::class,
        EventMainJudgeCrossRef::class,
        FightEntity::class,
        TournamentEntity::class,
    ],
    version = 29,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun authDao(): JudgeDao
    abstract fun fighterDao(): FighterDao
    abstract fun eventDao(): EventDao
    abstract fun eventParticipantsDao(): EventParticipantsDao
}

class DatabaseError(message: String?, cause: Throwable? = null) : Exception(message, cause)
