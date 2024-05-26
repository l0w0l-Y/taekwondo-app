package com.taekwondo.coredata.network.model

data class FightModel(
    val tournamentId: Long,
    val eventId: Long,
    val fighterId1: Long,
    val fighterId2: Long,
    val judgeId: Long,
    val points1: Int,
    val points2: Int,
    val id: Long = 0,
)