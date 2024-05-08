package com.taekwondo.coredata.network.repository

import com.taekwondo.coredata.network.Completable
import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.Error
import com.taekwondo.coredata.network.Success
import com.taekwondo.coredata.network.callDB
import com.taekwondo.coredata.network.dao.AuthDao
import com.taekwondo.coredata.network.database.DatabaseError
import com.taekwondo.coredata.network.di.IoDispatcher
import com.taekwondo.coredata.network.entity.UserEntity
import com.taekwondo.coredata.network.mapEffect
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface AuthRepository {
    suspend fun auth(email: String, password: String): Effect<Completable>
    suspend fun register(
        name: String,
        surname: String,
        email: String,
        password: String,
    ): Effect<Unit>
}

class AuthRepositoryImpl @Inject constructor(
    private val dao: AuthDao,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : AuthRepository {
    override suspend fun auth(email: String, password: String): Effect<Completable> {
        return callDB(dispatcher) {
            dao.getUserByEmail(email)
        }.mapEffect { user ->
            if (user != null && user.password == password) {
                Success(Completable)
            } else {
                Error(DatabaseError("Incorrect password"))
            }
        }
    }

    override suspend fun register(
        name: String,
        surname: String,
        email: String,
        password: String,
    ): Effect<Unit> {
        return callDB(dispatcher) {
            dao.insert(
                UserEntity(
                    email = email,
                    password = password,
                    name = name,
                    surname = surname
                )
            )
        }
    }
}