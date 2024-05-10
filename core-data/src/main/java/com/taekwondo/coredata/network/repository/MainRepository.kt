package com.taekwondo.coredata.network.repository

import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.Error
import com.taekwondo.coredata.network.Success
import com.taekwondo.coredata.network.callDB
import com.taekwondo.coredata.network.dao.EventDao
import com.taekwondo.coredata.network.dao.FighterDao
import com.taekwondo.coredata.network.database.DataStoreProvider
import com.taekwondo.coredata.network.database.UID_KEY
import com.taekwondo.coredata.network.di.IoDispatcher
import com.taekwondo.coredata.network.entity.EventEntity
import com.taekwondo.coredata.network.entity.FighterEntity
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface MainRepository {
    suspend fun getAllFighters(): Effect<List<FighterEntity>>
    suspend fun getAllEvents(): Effect<List<EventEntity>>
    suspend fun logOut(): Effect<Unit>
}

class MainRepositoryImpl @Inject constructor(
    private val fighterDao: FighterDao,
    private val eventDao: EventDao,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val dataStoreProvider: DataStoreProvider,
) : MainRepository {
    override suspend fun getAllFighters(): Effect<List<FighterEntity>> {
        return callDB(dispatcher) {
            fighterDao.getAllFighters()
        }
    }

    override suspend fun getAllEvents(): Effect<List<EventEntity>> {
        return callDB(dispatcher) {
            eventDao.getAllEvents()
        }
    }

    override suspend fun logOut(): Effect<Unit> {
        try {
            dataStoreProvider.remove(UID_KEY)
            return Success(Unit)
        } catch (e: Exception) {
            return Error(e)
        }
    }
}