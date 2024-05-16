package com.taekwondo.featureevent.domain

import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.map
import com.taekwondo.coredata.network.repository.AuthRepository
import com.taekwondo.coredata.network.repository.EventRepository
import com.taekwondo.coredata.network.repository.FighterRepository
import com.taekwondo.featureevent.presentation.model.EventModel
import com.taekwondo.featureevent.presentation.model.FighterModel
import com.taekwondo.featureevent.presentation.model.JudgeModel
import javax.inject.Inject

interface EventInteractor {
    suspend fun createEvent(name: String, date: String, place: String): Effect<Unit>
    suspend fun updateEvent(uid: Int, name: String, date: String, place: String): Effect<Unit>
    suspend fun getAllEvents(): Effect<Unit>
    suspend fun insertEventParticipants(
        uid: Int,
        users: List<Int>,
        fighters: List<Int>
    ): Effect<Unit>

    suspend fun getEventModel(uid: Int): Effect<EventModel?>
    suspend fun getAllFighters(): Effect<List<FighterModel>>
    suspend fun getAllUsers(): Effect<List<JudgeModel>>
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
        uid: Int,
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
        uid: Int,
        users: List<Int>,
        fighters: List<Int>
    ): Effect<Unit> {
        return eventRepository.insertEventParticipants(uid, users, fighters)
    }

    override suspend fun getEventModel(uid: Int): Effect<EventModel?> {
        return eventRepository.getEventParticipants(uid).map {
            it?.let { eventParticipantsEntity ->
                EventModel(
                    uid = eventParticipantsEntity.event.uid,
                    name = eventParticipantsEntity.event.name,
                    date = eventParticipantsEntity.event.date,
                    place = eventParticipantsEntity.event.place,
                    fighters = eventParticipantsEntity.fighters,
                    users = eventParticipantsEntity.judges
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

    override suspend fun getAllUsers(): Effect<List<JudgeModel>> {
        return userRepository.getAllUsers().map { users ->
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
}