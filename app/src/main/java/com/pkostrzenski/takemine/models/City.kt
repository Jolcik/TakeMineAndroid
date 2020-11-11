package com.pkostrzenski.takemine.models

data class City(
    val id: Int,
    val name: String,
    val centerLat: Double,
    val centerLng: Double,
    val centerZoom: Double
)