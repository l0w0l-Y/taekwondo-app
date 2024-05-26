package com.taekwondo.featureevent.domain

import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.entity.EventStatus
import com.taekwondo.coredata.network.map
import com.taekwondo.coredata.network.model.FightModel
import com.taekwondo.coredata.network.model.FighterModel
import com.taekwondo.coredata.network.model.ResultFighterModel
import com.taekwondo.coredata.network.model.TournamentModel
import com.taekwondo.coredata.network.repository.AuthRepository
import com.taekwondo.coredata.network.repository.EventRepository
import com.taekwondo.coredata.network.repository.FighterRepository
import com.taekwondo.featureevent.presentation.model.EventModel
import com.taekwondo.featureevent.presentation.model.JudgeModel
import javax.inject.Inject
import com.taekwondo.featureevent.presentation.model.EventStatus as EventStatusModel

interface EventInteractor {
    suspend fun createEvent(name: String, date: String, place: String): Effect<Unit>
    suspend fun updateEvent(uid: Long, name: String, date: String, place: String): Effect<Unit>
    suspend fun getAllEvents(): Effect<Unit>
    suspend fun insertEventParticipants(
        uid: Long,
        users: List<Long>,
        fighters: List<Long>,
        mainJudges: Long,
    ): Effect<Unit>

    suspend fun getEventModel(uid: Long): Effect<EventModel?>
    suspend fun getAllFighters(): Effect<List<FighterModel>>
    suspend fun getAllJudges(): Effect<List<JudgeModel>>
    suspend fun savePoints(
        fight: FightModel
    ): Effect<Unit>

    suspend fun getFightersEvent(
        eventId: Long
    ): Effect<List<FighterModel>>

    suspend fun deleteEvent(eventId: Long): Effect<Unit>
    suspend fun archiveEvent(eventId: Long): Effect<Unit>
    suspend fun getResults(eventId: Long): Effect<List<ResultFighterModel>>
    suspend fun getTournamentEvent(eventId: Long): Effect<List<TournamentModel>>
    suspend fun getTournament(tournamentId: Long): Effect<TournamentModel?>
    suspend fun setTournament(fighters: List<Long>, eventId: Long): Effect<Unit>
    suspend fun getWinner(eventId: Long, matchId: Long): Effect<Unit>
}

class EventInteractorImpl @Inject constructor(
    private val eventRepository: EventRepository,
    private val userRepository: AuthRepository,
    private val fighterRepository: FighterRepository,
) : EventInteractor {
    override suspend fun createEvent(name: String, date: String, place: String): Effect<Unit> {
        return eventRepository.createEvent(name, date, place)
    }

    override suspend fun updateEvent(
        uid: Long,
        name: String,
        date: String,
        place: String
    ): Effect<Unit> {
        return eventRepository.updateEvent(uid, name, date, place)
    }

    override suspend fun getAllEvents(): Effect<Unit> {
        return eventRepository.getAllEvents()
    }

    override suspend fun insertEventParticipants(
        uid: Long,
        users: List<Long>,
        fighters: List<Long>,
        mainJudges: Long,
    ): Effect<Unit> {
        return eventRepository.insertEventParticipants(uid, users, fighters, mainJudges)
    }

    override suspend fun getEventModel(uid: Long): Effect<EventModel?> {
        return eventRepository.getEventParticipants(uid).map {
            it?.let { eventParticipantsEntity ->
                EventModel(
                    uid = eventParticipantsEntity.event.uid,
                    name = eventParticipantsEntity.event.name,
                    date = eventParticipantsEntity.event.date,
                    place = eventParticipantsEntity.event.place,
                    fighters = eventParticipantsEntity.fighters.map { fighter ->
                        FighterModel(
                            uid = fighter.uid,
                            name = fighter.name,
                            photo = fighter.photo,
                            isPicked = false
                        )
                    },
                    judges = eventParticipantsEntity.judges.map { entity ->
                        JudgeModel(
                            uid = entity.uid,
                            name = entity.name,
                            username = entity.username,
                            photo = entity.photo,
                            isPicked = false
                        )
                    },
                    mainJudge = eventParticipantsEntity.mainFighter?.let { entity ->
                        JudgeModel(
                            uid = entity.uid,
                            name = entity.name,
                            username = entity.username,
                            photo = entity.photo,
                            isPicked = false
                        )
                    },
                    status = when (eventParticipantsEntity.event.status) {
                        EventStatus.IN_PROGRESS -> EventStatusModel.IN_PROGRESS
                        EventStatus.FINISHED -> EventStatusModel.FINISHED
                    }
                )
            }
        }
    }

    override suspend fun getAllFighters(): Effect<List<FighterModel>> {
        return fighterRepository.getAllFighters().map { fighters ->
            fighters?.map {
                FighterModel(
                    uid = it.uid,
                    name = it.name,
                    photo = it.photo,
                    isPicked = false
                )
            } ?: emptyList()
        }
    }

    override suspend fun getAllJudges(): Effect<List<JudgeModel>> {
        return userRepository.getAllJudges().map { users ->
            users?.map {
                JudgeModel(
                    uid = it.uid,
                    name = it.name,
                    username = it.username,
                    photo = it.photo,
                    isPicked = false
                )
            } ?: emptyList()
        }
    }

    override suspend fun savePoints(fight: FightModel): Effect<Unit> {
        return eventRepository.savePoints(fight)
    }

    override suspend fun getFightersEvent(eventId: Long): Effect<List<FighterModel>> {
        return eventRepository.getFightersEvent(eventId).map { fighters ->
            fighters?.map {
                FighterModel(
                    uid = it.uid,
                    name = it.name,
                    photo = it.photo,
                    isPicked = false
                )
            } ?: emptyList()
        }
    }

    override suspend fun deleteEvent(eventId: Long): Effect<Unit> {
        return eventRepository.deleteEvent(eventId)
    }

    override suspend fun archiveEvent(eventId: Long): Effect<Unit> {
        return eventRepository.archiveEvent(eventId)
    }

    override suspend fun getResults(eventId: Long): Effect<List<ResultFighterModel>> {
        return eventRepository.getResults(eventId)
    }

    override suspend fun getTournamentEvent(eventId: Long): Effect<List<TournamentModel>> {
        return eventRepository.getTournamentEvent(eventId)
    }

    override suspend fun getTournament(tournamentId: Long): Effect<TournamentModel?> {
        return eventRepository.getTournament(tournamentId)
    }

    override suspend fun setTournament(fighters: List<Long>, eventId: Long): Effect<Unit> {
        return eventRepository.setTournament(fighters, eventId)
    }

    override suspend fun getWinner(eventId: Long, matchId: Long): Effect<Unit> {
        return eventRepository.getWinner(eventId, matchId)
    }
}