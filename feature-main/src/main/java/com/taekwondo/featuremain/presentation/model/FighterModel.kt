package com.taekwondo.featuremain.presentation.model

data class FighterModel(
    val uid: Long,
    val name: String,
    val age: Float,
    val weight: Float,
    val height: Float,
    val weightCategory: String,
    val photo: String?,
)