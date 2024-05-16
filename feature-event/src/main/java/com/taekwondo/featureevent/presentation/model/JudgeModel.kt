package com.taekwondo.featureevent.presentation.model

data class JudgeModel(
    val uid: Long,
    val name: String,
    val username: String,
    val photo: String,
    val isPicked: Boolean,
)