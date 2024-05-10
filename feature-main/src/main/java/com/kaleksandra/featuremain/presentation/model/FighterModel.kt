package com.kaleksandra.featuremain.presentation.model

data class FighterModel(
    val uid: Int,
    val name: String,
    val age: Float,
    val weight: Float,
    val height: Float,
    val weightCategory: String,
    val photo: String?,
)