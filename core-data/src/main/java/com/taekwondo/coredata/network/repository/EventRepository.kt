package com.taekwondo.coredata.network.repository

import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.callDB
import com.taekwondo.coredata.network.dao.EventDao
import com.taekwondo.coredata.network.di.IoDispatcher
import com.taekwondo.coredata.network.entity.EventEntity
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface EventRepository {
    suspend fun createEvent(name: String, date: String, place: String): Effect<Unit>
    suspend fun getAllEvents() : Effect<Unit>
}

class EventRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher, private val eventDao: EventDao
) : EventRepository {
    override suspend fun createEvent(name: String, date: String, place: String): Effect<Unit> {
        return callDB(ioDispatcher) {
            eventDao.insert(EventEntity(name = name, date = date, place = place))
        }
    }

    override suspend fun getAllEvents(): Effect<Unit> {
        return callDB(ioDispatcher) {
            eventDao.getAllEvents()
        }
    }
}