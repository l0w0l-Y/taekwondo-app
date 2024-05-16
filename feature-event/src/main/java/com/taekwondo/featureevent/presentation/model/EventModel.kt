package com.taekwondo.featureevent.presentation.model

import com.taekwondo.coredata.network.entity.FighterEntity
import com.taekwondo.coredata.network.entity.UserEntity

data class EventModel(
    val uid: Int,
    val name: String,
    val date: String,
    val place: String,
    var fighters: List<FighterEntity>,
    var users: List<UserEntity>,
)