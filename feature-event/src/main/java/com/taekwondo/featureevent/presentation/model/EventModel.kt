package com.taekwondo.featureevent.presentation.model

data class EventModel(
    val uid: Long,
    val name: String,
    val date: String,
    val place: String,
    val status: EventStatus,
    var fighters: List<FighterModel>,
    var judges: List<JudgeModel>,
    var mainJudge: JudgeModel?,
)

enum class EventStatus {
    IN_PROGRESS,
    FINISHED,
}