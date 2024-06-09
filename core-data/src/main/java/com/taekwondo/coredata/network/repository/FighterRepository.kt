package com.taekwondo.coredata.network.repository

import android.net.Uri
import com.taekwondo.coredata.network.Effect
import com.taekwondo.coredata.network.Error
import com.taekwondo.coredata.network.Success
import com.taekwondo.coredata.network.callDB
import com.taekwondo.coredata.network.dao.FighterDao
import com.taekwondo.coredata.network.database.DatabaseError
import com.taekwondo.coredata.network.di.IoDispatcher
import com.taekwondo.coredata.network.entity.FighterEntity
import com.taekwondo.coredata.network.enums.Gender
import com.taekwondo.coredata.network.excel.ExcelReader
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

class FighterRepositoryImpl @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val fighterDao: FighterDao,
    private val excelReader: ExcelReader,
) : FighterRepository {
    //Создание бойца
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
        return callDB(dispatcher) {
            fighterDao.insert(
                FighterEntity(
                    name = name,
                    age = age,
                    weight = weight,
                    height = height,
                    weightCategory = weightCategory,
                    photo = photo,
                    club = club,
                    trainer = trainer,
                    gender = gender
                )
            )
        }
    }

    //Получение всех бойцов
    override suspend fun getAllFighters(): Effect<List<FighterEntity>> {
        return callDB(dispatcher) {
            fighterDao.getAllFighters()
        }
    }

    //Получение бойца по идентификатору
    override suspend fun getFighter(uid: Long): Effect<FighterEntity> {
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

    //Обновление бойца
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
        return callDB(dispatcher) {
            val fighter = fighterDao.getFighter(uid)
            fighter?.let {
                fighterDao.update(
                    fighter.copy(
                        name = name,
                        age = age,
                        weight = weight,
                        height = height,
                        weightCategory = weightCategory,
                        photo = photo,
                        club = club,
                        trainer = trainer,
                        gender = gender,
                    )
                )
            }
        }
    }

    //Удаление бойца
    override suspend fun deleteFighter(uid: Long): Effect<Unit> {
        return callDB(dispatcher) {
            fighterDao.deleteFighter(uid)
        }
    }

    //Чтение файла Excel
    override suspend fun readExcelFile(filePath: Uri): Effect<Unit> {
        return callDB(dispatcher) {
            fighterDao.insertAll(
                excelReader.readExcelFile(filePath)
            )
        }
    }
}