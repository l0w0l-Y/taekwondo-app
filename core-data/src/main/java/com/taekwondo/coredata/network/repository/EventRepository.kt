package com.taekwondo.coredata.network.repository

import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.callDB
import com.taekwondo.coredata.network.dao.EventDao
import com.taekwondo.coredata.network.dao.EventParticipantsDao
import com.taekwondo.coredata.network.di.IoDispatcher
import com.taekwondo.coredata.network.entity.EventEntity
import com.taekwondo.coredata.network.entity.EventFighterCrossRef
import com.taekwondo.coredata.network.entity.EventJudgeCrossRef
import com.taekwondo.coredata.network.entity.EventParticipants
import com.taekwondo.coredata.network.entity.FighterEntity
import com.taekwondo.coredata.network.entity.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface EventRepository {
    suspend fun createEvent(name: String, date: String, place: String): Effect<Unit>
    suspend fun getAllEvents(): Effect<Unit>

    suspend fun insertEventParticipants(
        uidEvent: Int,
        users: List<Int>,
        fighters: List<Int>
    ): Effect<Unit>

    suspend fun updateEvent(
        uid: Int,
        name: String,
        date: String,
        place: String
    ): Effect<Unit>

    suspend fun getEventParticipants(uid: Int): Effect<EventParticipants?>
}

class EventRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val eventDao: EventDao,
    private val eventParticipantsDao: EventParticipantsDao,
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

    override suspend fun insertEventParticipants(
        uidEvent: Int,
        users: List<Int>,
        fighters: List<Int>
    ): Effect<Unit> {
        return callDB(ioDispatcher) {
            eventDao.getEvent(uidEvent)?.let { event ->
                users.forEach { user ->
                    eventParticipantsDao.insertEventJudgeCrossRef(
                        EventJudgeCrossRef(
                            eventId = event.uid,
                            judgeId = user
                        )
                    )
                }
                fighters.forEach { fighter ->
                    eventParticipantsDao.insertEventFighterCrossRef(
                        EventFighterCrossRef(
                            eventId = event.uid,
                            fighterId = fighter
                        )
                    )
                }
            }
        }
    }

    override suspend fun updateEvent(
        uid: Int,
        name: String,
        date: String,
        place: String
    ): Effect<Unit> {
        return callDB(ioDispatcher) {
            eventDao.update(
                EventEntity(
                    uid = uid,
                    name = name,
                    date = date,
                    place = place
                )
            )
        }
    }

    override suspend fun getEventParticipants(uid: Int): Effect<EventParticipants?> {
        return callDB(ioDispatcher) {
            eventParticipantsDao.getEventParticipants(uid).apply {
                if (this == null) {
                    val event = eventDao.getEvent(uid)
                    if (event != null) {
                        return@callDB EventParticipants(event, emptyList(), emptyList())
                    }
                }
            }
        }
    }
}