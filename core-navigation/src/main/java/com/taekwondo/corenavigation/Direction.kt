package com.taekwondo.corenavigation

sealed class Direction(val path: String)
object MainDirection : Direction("main")
object AuthDirection : Direction("auth")
object RegisterDirection : Direction("register")
object CreateFighterDirection : Direction("create_fighter")
object CreateEventDirection : Direction("create_event")
object UpdateFighterDirection : Direction("update_fighter")
object ReadFighterDirection : Direction("read_fighter")
object ReadEventDirection : Direction("read_event")
object UpdateEventDirection : Direction("update_event")
object UpdateEventFighterDirection : Direction("update_event_fighter")