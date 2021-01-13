package com.pkostrzenski.takemine.models

data class Notifier(
    val user: User,
    val locations: Set<Location>,
    val itemType: ItemType
)