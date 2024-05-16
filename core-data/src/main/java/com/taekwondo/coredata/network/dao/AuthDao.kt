package com.taekwondo.coredata.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.taekwondo.coredata.network.entity.JudgeEntity

@Dao
interface AuthDao {
    /**
     * Вставляет новый JudgeEntity в базу данных.
     * @param entity JudgeEntity, который нужно вставить.
     * @return Идентификатор строки созданного JudgeEntity.
     */
    @Insert
    fun insert(entity: JudgeEntity) : Long

    /**
     * Получает JudgeEntity по email.
     * @param email Email JudgeEntity.
     * @return JudgeEntity с указанным email или null.
     */
    @Query("SELECT * FROM judge WHERE email LIKE :email LIMIT 1")
    fun getUserByEmail(email: String): JudgeEntity?

    /**
     * Получает всех JudgeEntity из базы данных.
     * @return Список всех JudgeEntity.
     */
    @Query("SELECT * FROM judge")
    fun getAllJudges(): List<JudgeEntity>
}
