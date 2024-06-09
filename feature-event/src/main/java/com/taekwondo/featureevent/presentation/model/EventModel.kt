package com.taekwondo.featureevent.presentation.model

import com.taekwondo.coredata.network.entity.EventLevel
import com.taekwondo.coredata.network.model.FighterModel

data class EventModel(
    val uid: Long,
    val name: String,
    val date: String,
    val place: String,
    val city: String,
    val level: EventLevel,
    val status: EventStatus,
    var fighters: List<FighterModel>,
    var judges: List<JudgeModel>,
    var mainJudge: JudgeModel?,
)

enum class EventStatus {
    IN_PROGRESS,
    FINISHED,
}