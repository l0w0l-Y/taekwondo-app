package com.taekwondo.coredata.network.repository

import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.Error
import com.taekwondo.coredata.network.Success
import com.taekwondo.coredata.network.call
import com.taekwondo.coredata.network.callDB
import com.taekwondo.coredata.network.dao.FighterDao
import com.taekwondo.coredata.network.database.DatabaseError
import com.taekwondo.coredata.network.di.IoDispatcher
import com.taekwondo.coredata.network.entity.FighterEntity
import com.taekwondo.coredata.network.mapEffect
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface FighterRepository {
    suspend fun createFighter(
        name: String,
        age: Float,
        weight: Float,
        height: Float,
        weightCategory: String,
        photo: String?,
    ): Effect<Unit>

    suspend fun getAllFighters(): Effect<List<FighterEntity>>
    suspend fun getFighter(uid: Int): Effect<FighterEntity>
    suspend fun updateFighter(
        uid: Int,
        name: String,
        age: Float,
        weight: Float,
        height: Float,
        weightCategory: String,
        photo: String?,
    ): Effect<Unit>

    suspend fun deleteFighter(uid: Int): Effect<Unit>
}

class FighterRepositoryImpl @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val fighterDao: FighterDao,
) : FighterRepository {
    override suspend fun createFighter(
        name: String,
        age: Float,
        weight: Float,
        height: Float,
        weightCategory: String,
        photo: String?
    ): Effect<Unit> {
        return callDB(dispatcher) {
            fighterDao.insert(
                FighterEntity(
                    name = name,
                    age = age,
                    weight = weight,
                    height = height,
                    weightCategory = weightCategory,
                    photo = photo
                )
            )
        }
    }

    override suspend fun getAllFighters(): Effect<List<FighterEntity>> {
        return callDB(dispatcher) {
            fighterDao.getAllFighters()
        }
    }

    override suspend fun getFighter(uid: Int): Effect<FighterEntity> {
        return callDB(dispatcher) {
            fighterDao.getFighter(uid)
        }.mapEffect { user ->
            if (user != null) {
                Success(user)
            } else {
                Error(DatabaseError("Not found"))
            }
        }
    }

    override suspend fun updateFighter(
        uid: Int,
        name: String,
        age: Float,
        weight: Float,
        height: Float,
        weightCategory: String,
        photo: String?,
    ): Effect<Unit> {
        return callDB(dispatcher) {
            fighterDao.update(
                FighterEntity(
                    uid = uid,
                    name = name,
                    age = age,
                    weight = weight,
                    height = height,
                    weightCategory = weightCategory,
                    photo = photo,
                )
            )
        }
    }

    override suspend fun deleteFighter(uid: Int): Effect<Unit> {
        return callDB(dispatcher) {
            fighterDao.deleteFighter(uid)
        }
    }
}