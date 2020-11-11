package com.pkostrzenski.takemine.models

data class Product(
    val id: Int,
    val name: String,
    val address: String,
    val time: String,
    val date: String,
    val itemType: ItemType,
    val buyer: User?
)