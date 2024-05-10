package com.taekwondo.featureevent.domain

import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.repository.EventRepository
import javax.inject.Inject

interface EventInteractor {
    suspend fun createEvent(name: String, date: String, place: String): Effect<Unit>
    suspend fun getAllEvents(): Effect<Unit>
}

class EventInteractorImpl @Inject constructor(
    private val eventRepository: EventRepository
) : EventInteractor {
    override suspend fun createEvent(name: String, date: String, place: String): Effect<Unit> {
        return eventRepository.createEvent(name, date, place)
    }

    override suspend fun getAllEvents(): Effect<Unit> {
        return eventRepository.getAllEvents()
    }

}