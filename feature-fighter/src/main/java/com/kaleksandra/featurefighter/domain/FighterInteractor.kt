package com.kaleksandra.featurefighter.domain

import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.entity.FighterEntity
import com.taekwondo.coredata.network.repository.FighterRepository
import javax.inject.Inject

interface FighterInteractor {
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

class FighterInteractorImpl @Inject constructor(
    private val fighterRepository: FighterRepository
) : FighterInteractor {
    override suspend fun createFighter(
        name: String,
        age: Float,
        weight: Float,
        height: Float,
        weightCategory: String,
        photo: String?,
    ): Effect<Unit> {
        return fighterRepository.createFighter(name, age, weight, height, weightCategory, photo)
    }

    override suspend fun getAllFighters(): Effect<List<FighterEntity>> {
        return fighterRepository.getAllFighters()
    }

    override suspend fun getFighter(uid: Int): Effect<FighterEntity> {
        return fighterRepository.getFighter(uid)
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
        return fighterRepository.updateFighter(
            uid,
            name,
            age,
            weight,
            height,
            weightCategory,
            photo
        )
    }

    override suspend fun deleteFighter(uid: Int): Effect<Unit> {
        return fighterRepository.deleteFighter(uid)
    }
}