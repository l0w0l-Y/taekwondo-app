package com.taekwondo.coredata.network.repository

import com.taekwondo.coredata.network.Completable
import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.Error
import com.taekwondo.coredata.network.Success
import com.taekwondo.coredata.network.callDB
import com.taekwondo.coredata.network.dao.AuthDao
import com.taekwondo.coredata.network.database.DataStoreProvider
import com.taekwondo.coredata.network.database.DatabaseError
import com.taekwondo.coredata.network.database.UID_KEY
import com.taekwondo.coredata.network.di.IoDispatcher
import com.taekwondo.coredata.network.entity.UserEntity
import com.taekwondo.coredata.network.mapEffect
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AuthRepository {
    suspend fun auth(email: String, password: String): Effect<Completable>
    suspend fun register(
        name: String,
        username: String,
        email: String,
        password: String,
        phone: String,
        photo: String,
    ): Effect<Unit>
}

class AuthRepositoryImpl @Inject constructor(
    private val dao: AuthDao,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val dataStoreProvider: DataStoreProvider,
) : AuthRepository {
    override suspend fun auth(email: String, password: String): Effect<Completable> {
        return callDB(dispatcher) {
            dao.getUserByEmail(email)
        }.mapEffect { user ->
            if (user != null && user.password == password) {
                withContext(dispatcher) { dataStoreProvider.update(UID_KEY, user.uid) }
                Success(Completable)
            } else {
                Error()
            }
        }
    }

    override suspend fun register(
        name: String,
        username: String,
        email: String,
        password: String,
        phone: String,
        photo: String
    ): Effect<Unit> {
        return callDB(dispatcher) {
            if (dao.getUserByEmail(email) != null) {
                Error<UserEntity>()
            } else {
                dao.insert(
                    UserEntity(
                        email = email,
                        password = password,
                        name = name,
                        username = username,
                        phone = phone,
                        photo = photo,
                    )
                )
            }
        }
    }
}