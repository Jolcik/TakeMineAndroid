package com.pkostrzenski.takemine.models

data class Product(
    val id: Int?,
    val name: String,
    val description: String?,
    val city: City,
    val locations: Set<Location>,
    val itemType: ItemType,
    val pictures: Set<Picture>,
    val buyer: User?
)