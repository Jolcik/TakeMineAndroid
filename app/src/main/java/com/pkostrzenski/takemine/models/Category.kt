package com.pkostrzenski.takemine.models

data class Category(
    val id: Int,
    val name: String,
    val items: List<ItemType>
)