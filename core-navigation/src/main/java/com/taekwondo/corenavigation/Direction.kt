package com.taekwondo.corenavigation

sealed class Direction(val path: String)
object MainDirection : Direction("main")
object AuthDirection : Direction("auth")
object RegisterDirection : Direction("register")