package com.taekwondo.coredata.network.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Сущность, представляющая результат боя.
 * @param id Идентификатор результата.
 * @param eventId Идентификатор события.
 * @param winner Идентификатор победителя.
 * @param loser Идентификатор проигравшего.
 */
@Entity(tableName = "result")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventId: Long,
    val winner: Long,
    val loser: Long,
)