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
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface EventRepository {
    suspend fun createEvent(name: String, date: String, place: String): Effect<Unit>
    suspend fun getAllEvents(): Effect<Unit>

    suspend fun insertEventParticipants(
        uidEvent: Long,
        users: List<Long>,
        fighters: List<Long>
    ): Effect<Unit>

    suspend fun updateEvent(
        uid: Long,
        name: String,
        date: String,
        place: String
    ): Effect<Unit>

    suspend fun getEventParticipants(uid: Long): Effect<EventParticipants?>
}

class EventRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val eventDao: EventDao,
    private val eventParticipantsDao: EventParticipantsDao,
) : EventRepository {

    //Создание события
    override suspend fun createEvent(name: String, date: String, place: String): Effect<Unit> {
        return callDB(ioDispatcher) {
            eventDao.insert(EventEntity(name = name, date = date, place = place))
        }
    }

    //Получение всех событий
    override suspend fun getAllEvents(): Effect<Unit> {
        return callDB(ioDispatcher) {
            eventDao.getAllEvents()
        }
    }

    //Добавление участников события
    override suspend fun insertEventParticipants(
        uidEvent: Long,
        users: List<Long>,
        fighters: List<Long>
    ): Effect<Unit> {
        return callDB(ioDispatcher) {
            eventDao.getEvent(uidEvent)?.let { event ->
                eventParticipantsDao.removeEventFighterCrossRef(event.uid)
                eventParticipantsDao.removeEventJudgeCrossRef(event.uid)
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

    //Обновление события
    override suspend fun updateEvent(
        uid: Long,
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

    //Получение участников события
    override suspend fun getEventParticipants(uid: Long): Effect<EventParticipants?> {
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