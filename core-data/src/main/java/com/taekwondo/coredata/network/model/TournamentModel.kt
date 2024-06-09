package com.taekwondo.coredata.network.model

data class TournamentModel(
    val id: Long,
    val eventId: Long,
    val fighter1: FighterModel?,
    val fighter2: FighterModel?,
    val round: Int,
    val group: Int,
    val winnerIndex: Long?,
    val judgeChoices: List<JudgeChoiceModel>,
)