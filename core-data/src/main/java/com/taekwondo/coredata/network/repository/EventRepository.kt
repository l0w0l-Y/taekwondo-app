package com.taekwondo.coredata.network.repository

import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.callDB
import com.taekwondo.coredata.network.dao.EventDao
import com.taekwondo.coredata.network.dao.EventParticipantsDao
import com.taekwondo.coredata.network.dao.FighterDao
import com.taekwondo.coredata.network.di.IoDispatcher
import com.taekwondo.coredata.network.entity.EventEntity
import com.taekwondo.coredata.network.entity.EventFighterCrossRef
import com.taekwondo.coredata.network.entity.EventJudgeCrossRef
import com.taekwondo.coredata.network.entity.EventMainJudgeCrossRef
import com.taekwondo.coredata.network.entity.EventParticipants
import com.taekwondo.coredata.network.entity.EventStatus
import com.taekwondo.coredata.network.entity.FighterEntity
import com.taekwondo.coredata.network.entity.ResultEntity
import com.taekwondo.coredata.network.model.FightModel
import com.taekwondo.coredata.network.model.ResultFighterModel
import com.taekwondo.coredata.network.model.ResultModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface EventRepository {
    suspend fun createEvent(name: String, date: String, place: String): Effect<Unit>
    suspend fun getAllEvents(): Effect<Unit>
    suspend fun insertEventParticipants(
        uidEvent: Long,
        users: List<Long>,
        fighters: List<Long>,
        mainJudges: Long,
    ): Effect<Unit>

    suspend fun updateEvent(
        uid: Long,
        name: String,
        date: String,
        place: String
    ): Effect<Unit>

    suspend fun getEventParticipants(uid: Long): Effect<EventParticipants?>
    suspend fun savePoints(
        fight: FightModel
    ): Effect<Unit>

    suspend fun getFightersEvent(eventId: Long): Effect<List<FighterEntity>>
    suspend fun deleteEvent(eventId: Long): Effect<Unit>
    suspend fun archiveEvent(eventId: Long): Effect<Unit>
    suspend fun getResults(eventId: Long): Effect<List<ResultFighterModel>>
}

class EventRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val eventDao: EventDao,
    private val eventParticipantsDao: EventParticipantsDao,
    private val fighterDao: FighterDao,
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
        fighters: List<Long>,
        mainJudges: Long,
    ): Effect<Unit> {
        return callDB(ioDispatcher) {
            eventDao.getEvent(uidEvent)?.let { event ->
                eventParticipantsDao.removeEventMainJudgeCrossRef(event.uid)
                eventParticipantsDao.removeEventJudgeCrossRef(event.uid)
                eventParticipantsDao.removeEventFighterCrossRef(event.uid)
                eventParticipantsDao.insertEventMainJudgeCrossRef(
                    EventMainJudgeCrossRef(
                        eventId = event.uid,
                        judgeId = mainJudges,
                    )
                )
                users.forEach { user ->
                    eventParticipantsDao.insertEventJudgeCrossRef(
                        EventJudgeCrossRef(
                            eventId = event.uid,
                            judgeId = user,
                        )
                    )
                }
                fighters.forEach { fighter ->
                    eventParticipantsDao.insertEventFighterCrossRef(
                        EventFighterCrossRef(
                            eventId = event.uid,
                            fighterId = fighter,
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
                        return@callDB EventParticipants(event, emptyList(), emptyList(), null)
                    }
                }
            }
        }
    }

    override suspend fun savePoints(fight: FightModel): Effect<Unit> {
        return callDB(ioDispatcher) {
            eventDao.upsertFight(
                fight.judgeId,
                fight.eventId,
                fight.fighterId1,
                fight.fighterId2,
                fight.points1,
                fight.points2
            )
        }
    }

    override suspend fun getFightersEvent(eventId: Long): Effect<List<FighterEntity>> {
        return callDB(ioDispatcher) {
            eventParticipantsDao.getFightersEvent(eventId)
        }
    }

    override suspend fun deleteEvent(eventId: Long): Effect<Unit> {
        return callDB(ioDispatcher) {
            eventDao.deleteEvent(eventId)
        }
    }

    override suspend fun archiveEvent(eventId: Long): Effect<Unit> {
        val results = mutableListOf<ResultModel>()
        val map = mutableMapOf<Pair<Long, Long>, MutableList<Pair<Long, Long>>>()
        return callDB(ioDispatcher) {
            eventDao.getEvent(eventId)?.let {
                eventDao.update(it.copy(status = EventStatus.FINISHED))
            }
            val _fights = eventDao.getFightsByEventId(eventId)
            _fights.forEach {
                if (it.fighterId1 < it.fighterId2) {
                    map[Pair(it.fighterId1, it.fighterId2)] =
                        (map[Pair(it.fighterId1, it.fighterId2)]
                            ?: mutableListOf()).apply {
                            add(
                                Pair(
                                    it.points1.toLong(),
                                    it.points2.toLong()
                                )
                            )
                        }
                } else if (it.fighterId1 > it.fighterId2) {
                    map[Pair(it.fighterId2, it.fighterId1)] =
                        (map[Pair(it.fighterId2, it.fighterId1)]
                            ?: mutableListOf()).apply {
                            add(
                                Pair(
                                    it.points2.toLong(),
                                    it.points1.toLong()
                                )
                            )
                        }
                }
            }
            for (key in map.keys) {
                results.add(
                    if (map[key]!!.count { it.first > it.second } > map[key]!!.size / 2) {
                        ResultModel(
                            winner = key.first,
                            loser = key.second,
                            isDraw = false,
                        )
                    } else if (map[key]!!.count { it.first < it.second } > map[key]!!.size / 2) {
                        ResultModel(
                            winner = key.second,
                            loser = key.first,
                            isDraw = false,
                        )
                    } else {
                        ResultModel(
                            winner = key.first,
                            loser = key.second,
                            isDraw = true,
                        )
                    }
                )
            }
            for (result in results) {
                eventDao.insertResult(
                    ResultEntity(
                        eventId = eventId,
                        winner = result.winner,
                        loser = result.loser,
                    )
                )
            }
        }
    }

    override suspend fun getResults(eventId: Long): Effect<List<ResultFighterModel>> {
        return callDB(ioDispatcher) {
            eventDao.getAllResults(eventId).map {
                ResultFighterModel(
                    winner = fighterDao.getFighter(it.winner)?.name ?: "",
                    loser = fighterDao.getFighter(it.loser)?.name ?: "",
                )
            }
        }
    }
}