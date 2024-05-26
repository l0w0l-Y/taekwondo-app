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
import com.taekwondo.coredata.network.entity.TournamentEntity
import com.taekwondo.coredata.network.model.FightModel
import com.taekwondo.coredata.network.model.FighterModel
import com.taekwondo.coredata.network.model.ResultFighterModel
import com.taekwondo.coredata.network.model.TournamentModel
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
    suspend fun getTournamentEvent(eventId: Long): Effect<List<TournamentModel>>
    suspend fun getTournament(tournamentId: Long): Effect<TournamentModel?>
    suspend fun setTournament(fighters: List<Long>, eventId: Long): Effect<Unit>
    suspend fun getWinner(eventId: Long, matchId: Long): Effect<Unit>
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
                fight.tournamentId,
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
        return callDB(ioDispatcher) {
            eventDao.getEvent(eventId)?.let {
                eventDao.update(it.copy(status = EventStatus.FINISHED))
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

    override suspend fun getTournamentEvent(eventId: Long): Effect<List<TournamentModel>> {
        return callDB(ioDispatcher) {
            eventDao.getTournamentEvent(eventId).map {
                TournamentModel(
                    id = it.uid,
                    eventId = it.eventId,
                    fighter1 = it.fighterId1?.let { it1 ->
                        fighterDao.getFighter(it1)?.let { fighter ->
                            FighterModel(
                                uid = fighter.uid,
                                name = fighter.name,
                                photo = fighter.photo,
                                isPicked = false
                            )
                        }
                    },
                    fighter2 = it.fighterId2?.let { it1 ->
                        fighterDao.getFighter(it1)?.let { fighter ->
                            FighterModel(
                                uid = fighter.uid,
                                name = fighter.name,
                                photo = fighter.photo,
                                isPicked = false
                            )
                        }
                    },
                    group = it.group,
                    round = it.round,
                    winnerIndex = it.winnerIndex,
                )
            }
        }
    }

    override suspend fun getTournament(tournamentId: Long): Effect<TournamentModel?> {
        return callDB(ioDispatcher) {
            eventDao.getTournament(tournamentId)?.let {
                TournamentModel(
                    id = it.uid,
                    eventId = it.eventId,
                    fighter1 = it.fighterId1?.let { it1 ->
                        fighterDao.getFighter(it1)?.let { fighter ->
                            FighterModel(
                                uid = fighter.uid,
                                name = fighter.name,
                                photo = fighter.photo,
                                isPicked = false
                            )
                        }
                    },
                    fighter2 = it.fighterId2?.let { it1 ->
                        fighterDao.getFighter(it1)?.let { fighter ->
                            FighterModel(
                                uid = fighter.uid,
                                name = fighter.name,
                                photo = fighter.photo,
                                isPicked = false
                            )
                        }
                    },
                    group = it.group,
                    round = it.round,
                    winnerIndex = it.winnerIndex,
                )
            }
        }
    }

    override suspend fun setTournament(fighters: List<Long>, eventId: Long): Effect<Unit> {
        return callDB(ioDispatcher) {
            if (eventDao.getTournamentEvent(eventId).isNotEmpty()) {
                return@callDB
            }
            var round = 0
            var fightersSize = fighters.size
            while (fightersSize > 1) {
                fightersSize /= 2
                round++
            }
            val groups = fighters.shuffled().chunked(2)
            groups.forEachIndexed { index, group ->
                eventDao.insertTournament(
                    TournamentEntity(
                        eventId = eventId,
                        fighterId1 = group.getOrNull(0),
                        fighterId2 = group.getOrNull(1),
                        group = index,
                        round = round,
                        winnerIndex = null,
                    )
                )
            }
        }
    }

    override suspend fun getWinner(eventId: Long, matchId: Long): Effect<Unit> {
        return callDB(ioDispatcher) {
            var first = 0
            var second = 0
            eventDao.getFightsByTournamentId(matchId).forEach {
                if (it.points1 > it.points2) {
                    first++
                } else {
                    second++
                }
            }
            val tournament = eventDao.getTournament(matchId)
            if (tournament != null) {
                val nextTournament = eventDao.getTournament(
                    eventId,
                    tournament.round - 1,
                    tournament.group / 2
                )
                if (first == second) return@callDB
                if (first > second) {
                    eventDao.insertTournament(tournament.copy(winnerIndex = 0))
                    if (nextTournament != null) {
                        eventDao.insertTournament(
                            nextTournament.copy(
                                fighterId2 = tournament.fighterId1
                            )
                        )
                    } else {
                        eventDao.insertTournament(
                            TournamentEntity(
                                eventId = eventId,
                                fighterId1 = tournament.fighterId1,
                                fighterId2 = null,
                                group = tournament.group / 2,
                                round = tournament.round - 1,
                                winnerIndex = null,
                            )
                        )
                    }
                } else {
                    eventDao.insertTournament(tournament.copy(winnerIndex = 1))
                    if (nextTournament != null) {
                        eventDao.insertTournament(
                            nextTournament.copy(
                                fighterId2 = tournament.fighterId2
                            )
                        )
                    } else {
                        eventDao.insertTournament(
                            TournamentEntity(
                                eventId = eventId,
                                fighterId1 = tournament.fighterId2,
                                fighterId2 = null,
                                group = tournament.group / 2,
                                round = tournament.round - 1,
                                winnerIndex = null,
                            )
                        )
                    }
                }
            }
        }
    }
}