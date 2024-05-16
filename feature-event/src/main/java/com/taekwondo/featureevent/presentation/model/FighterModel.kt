package com.taekwondo.featureevent.presentation.model

data class FighterModel(
    val uid: Int,
    val name:String,
    val photo: String?,
    val isPicked: Boolean,
)