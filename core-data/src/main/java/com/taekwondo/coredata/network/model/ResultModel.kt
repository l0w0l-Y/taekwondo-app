package com.taekwondo.coredata.network.model

data class ResultModel(
    val winner: Long,
    val loser: Long,
    val isDraw: Boolean
)