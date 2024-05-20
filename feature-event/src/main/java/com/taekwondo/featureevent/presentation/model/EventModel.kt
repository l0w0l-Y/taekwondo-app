package com.taekwondo.featureevent.presentation.model

import com.taekwondo.coredata.network.entity.FighterEntity
import com.taekwondo.coredata.network.entity.JudgeEntity

data class EventModel(
    val uid: Long,
    val name: String,
    val date: String,
    val place: String,
    val status: EventStatus,
    var fighters: List<FighterEntity>,
    var users: List<JudgeEntity>,
)

enum class EventStatus {
    IN_PROGRESS,
    FINISHED,
}