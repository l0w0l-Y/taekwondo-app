package com.taekwondo.featureevent.presentation.model

data class FighterModel(
    val uid: Long,
    val name: String,
    val photo: String?,
    val isPicked: Boolean,
)