package com.taekwondo.featureauth.domain

import com.taekwondo.coredata.network.Completable
import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.repository.AuthRepository
import javax.inject.Inject

interface AuthInteractor {
    suspend fun auth(email: String, password: String): Effect<Completable>
    suspend fun register(
        name: String,
        username: String,
        email: String,
        password: String,
        phone: String,
        photo: String
    ): Effect<Unit>
}

class AuthInteractorImpl @Inject constructor(
    private val authRepository: AuthRepository,
) : AuthInteractor {
    override suspend fun auth(email: String, password: String): Effect<Completable> {
        return authRepository.auth(email, password)
    }

    override suspend fun register(
        name: String,
        username: String,
        email: String,
        password: String,
        phone: String,
        photo: String
    ): Effect<Unit> {
        return authRepository.register(name, username, email, password, phone, photo)
    }
}