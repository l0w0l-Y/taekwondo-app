package com.taekwondo.featurefighter.domain

import android.net.Uri
import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.entity.FighterEntity
import com.taekwondo.coredata.network.enums.Gender
import com.taekwondo.coredata.network.repository.FighterRepository
import javax.inject.Inject

interface FighterInteractor {
    suspend fun createFighter(
        name: String,
        age: Float,
        weight: Float,
        height: Float,
        weightCategory: String,
        gender: Gender,
        club: String,
        trainer: String,
        photo: String?
    ): Effect<Unit>

    suspend fun getAllFighters(): Effect<List<FighterEntity>>
    suspend fun getFighter(uid: Long): Effect<FighterEntity>
    suspend fun updateFighter(
        uid: Long,
        name: String,
        age: Float,
        weight: Float,
        height: Float,
        weightCategory: String,
        gender: Gender,
        club: String,
        trainer: String,
        photo: String?
    ): Effect<Unit>

    suspend fun deleteFighter(uid: Long): Effect<Unit>
    suspend fun readExcelFile(filePath: Uri): Effect<Unit>
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
        gender: Gender,
        club: String,
        trainer: String,
        photo: String?
    ): Effect<Unit> {
        return fighterRepository.createFighter(
            name,
            age,
            weight,
            height,
            weightCategory,
            gender,
            club,
            trainer,
            photo
        )
    }

    override suspend fun getAllFighters(): Effect<List<FighterEntity>> {
        return fighterRepository.getAllFighters()
    }

    override suspend fun getFighter(uid: Long): Effect<FighterEntity> {
        return fighterRepository.getFighter(uid)
    }

    override suspend fun updateFighter(
        uid: Long,
        name: String,
        age: Float,
        weight: Float,
        height: Float,
        weightCategory: String,
        gender: Gender,
        club: String,
        trainer: String,
        photo: String?
    ): Effect<Unit> {
        return fighterRepository.updateFighter(
            uid,
            name,
            age,
            weight,
            height,
            weightCategory,
            gender,
            club,
            trainer,
            photo
        )
    }

    override suspend fun deleteFighter(uid: Long): Effect<Unit> {
        return fighterRepository.deleteFighter(uid)
    }

    override suspend fun readExcelFile(filePath: Uri): Effect<Unit> {
        return fighterRepository.readExcelFile(filePath)
    }
}