package com.taekwondo.coredata.network.model

data class FightModel(
    val eventId: Long,
    val fighterId: Long,
    val judgeId: Long,
    val points: Int,
    val round: Int,
    val id: Long = 0,
)